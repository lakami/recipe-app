package dev.pjatk.recipeapp.usecase;

import com.nulabinc.zxcvbn.Zxcvbn;
import dev.pjatk.recipeapp.dto.request.RegisterDTO;
import dev.pjatk.recipeapp.service.EmailAlreadyUsedException;
import dev.pjatk.recipeapp.service.EmailService;
import dev.pjatk.recipeapp.service.IUserService;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegisterUseCase implements Loggable {
    private final IUserService userService;
    private final EmailService emailService;

    public void register(RegisterDTO registerDTO) {
        try {
            log().info("Registering user with email: {}", registerDTO.email());
            checkPassword(registerDTO.password());
            var user = userService.register(registerDTO.email(), registerDTO.password());
            log().info("User registered: {}", user.getEmail());
            log().info("Activation token: {}", user.getActivationToken()); // TODO: send email, logging activation token for now
            emailService.senActivationEmail(user);
        } catch (EmailAlreadyUsedException e) {
            log().warn("Someone tried to register with an already used email: {}", registerDTO.email());
        }
    }

    //TODO: password validation can be moved to validation layer
    private void checkPassword(String password) {
        Zxcvbn zxcvbn = new Zxcvbn();
        var strength = zxcvbn.measure(password);
        if (strength.getScore() < 3) {
            log().warn("Password too weak: {}", strength.getFeedback().getWarning());
            throw new TooWeakPasswordException();
        }
    }
}
