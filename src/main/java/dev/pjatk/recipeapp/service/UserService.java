package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.ActivationDTO;
import dev.pjatk.recipeapp.entity.Authority;
import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.repository.AuthorityRepository;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.security.Authorities;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Transactional
@RequiredArgsConstructor
@Service
class UserService implements IUserService {

    private static final SecureRandom SECURE_RANDOM;

    static {
        SECURE_RANDOM = new SecureRandom();
        SECURE_RANDOM.nextBytes(new byte[64]);
    }

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(String email, String password) throws EmailAlreadyUsedException {
        var maybeRegisteredUser = userRepository.findByEmail(email);
        if (maybeRegisteredUser.isPresent()) {
            // TODO: handle activated and not activated users differently, maybe delete not activated user and let the new one register, schedule deletion!
            throw new EmailAlreadyUsedException();
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setActivationToken(generateToken());
        user.setProfileUrl(UUID.randomUUID().toString()); // temporary value, will be updated later on activation
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(Authorities.USER).ifPresent(authorities::add);
        user.setAuthorities(authorities);
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> activate(String activationToken, ActivationDTO missingData) {
        return userRepository.findByActivationToken(activationToken)
                .map(user -> {
                    user.setActivated(true);
                    user.setActivationToken(null); // very important to remove token after activation
                    user.setFirstName(missingData.firstName());
                    user.setLastName(missingData.lastName());
                    return user;
                });
    }

    @Override
    public String findNextProfileUrl(String firstName, String lastName) {
        String profileUrl = firstName + "-" + lastName;
        if (userRepository.existsByProfileUrl(profileUrl)) {
            return profileUrl + "-" + RandomStringUtils.random(5, 0, 0, true, true, null, SECURE_RANDOM);
        }
        return profileUrl;
    }

    @Override
    public Optional<User> updateProfileUrl(String email, String profileUrl) {
        return userRepository.findByEmail(email)
                .filter(__ -> !userRepository.existsByProfileUrl(profileUrl)) // make sure profileUrl is unique
                .map(user -> {
                    user.setProfileUrl(profileUrl);
                    userRepository.save(user);
                    return user;
                });
    }

    private String generateToken() {
        return RandomStringUtils.random(20, 0, 0, true, true, null, SECURE_RANDOM);
    }

    @Override
    public Optional<User> getUserWithRecipes(String profileUrl) {
        return userRepository.findOneWithRecipesByProfileUrl(profileUrl)
                .map(user -> {
                    user.getRecipes().size(); // force fetch
                    return user;
                });
    }
}
