package dev.pjatk.recipeapp.usecase.user;

import dev.pjatk.recipeapp.entity.Authority;
import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.exception.EmailAlreadyUsedException;
import dev.pjatk.recipeapp.repository.AuthorityRepository;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.util.security.Authorities;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CreateUserUseCase {

    private static final SecureRandom SECURE_RANDOM;

    static {
        SECURE_RANDOM = new SecureRandom();
        SECURE_RANDOM.nextBytes(new byte[64]);
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public User execute(String email, String password) throws EmailAlreadyUsedException {
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

    private String generateToken() {
        return RandomStringUtils.random(20, 0, 0, true, true, null, SECURE_RANDOM);
    }
}
