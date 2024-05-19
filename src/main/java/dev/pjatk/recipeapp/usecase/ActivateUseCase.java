package dev.pjatk.recipeapp.usecase;

import dev.pjatk.recipeapp.dto.request.ActivationDTO;
import dev.pjatk.recipeapp.service.IUserService;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ActivateUseCase implements Loggable {

    private final IUserService userService;

    public void activate(ActivationDTO activationDTO, String token) {
        userService.activate(token, activationDTO)
                .ifPresentOrElse(activated -> {
                    log().info("User activated: {}", activated.getEmail());
                    // TODO: decide whether authenticate user automatically or not
                    // TODO: update profile url
                    var profileUrl = userService.findNextProfileUrl(activated.getFirstName(),
                                                                    activated.getLastName());
                    userService.updateProfileUrl(activated.getEmail(), profileUrl);
                }, () -> {
                    log().warn("No user found for token: {}", token);
                    throw new NoUserFoundException();
                });
    }
}
