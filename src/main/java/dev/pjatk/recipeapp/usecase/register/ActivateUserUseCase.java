package dev.pjatk.recipeapp.usecase.register;

import dev.pjatk.recipeapp.dto.request.ActivationDTO;
import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.exception.NoUserFoundException;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.usecase.user.FindNextProfileUrlUseCase;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Transactional
public class ActivateUserUseCase implements Loggable {
    private final UserRepository userRepository;
    private final FindNextProfileUrlUseCase findNextProfileUrlUseCase;

    // TODO: decide whether authenticate user automatically or not
    public void execute(String token, ActivationDTO missingData) {
        userRepository.findByActivationToken(token)
                .map(user -> {
                    user.setActivated(true);
                    user.setActivationToken(null); // very important to remove token after activation
                    user.setFirstName(missingData.firstName());
                    user.setLastName(missingData.lastName());
                    return user;
                })
                .ifPresentOrElse(activated -> {
                    log().info("User activated: {}", activated.getEmail());
                    var profileUrl = findNextProfileUrlUseCase.execute(activated.getFirstName(),
                                                                       activated.getLastName());
                    updateProfileUrl(activated.getEmail(), profileUrl)
                            .ifPresentOrElse(user -> log().info("Profile URL updated: {}", user.getProfileUrl()),
                                             () -> log().warn("Profile URL not updated"));
                }, () -> {
                    log().warn("No user found for token: {}", token);
                    throw new NoUserFoundException();
                });
    }

    private Optional<User> updateProfileUrl(String email, String profileUrl) {
        return userRepository.findByEmail(email)
                .filter(__ -> !userRepository.existsByProfileUrl(profileUrl)) // make sure profileUrl is unique
                .map(user -> {
                    user.setProfileUrl(profileUrl);
                    userRepository.save(user);
                    return user;
                });
    }
}
