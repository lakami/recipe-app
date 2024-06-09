package dev.pjatk.recipeapp.usecase.register;

import com.nulabinc.zxcvbn.Zxcvbn;
import dev.pjatk.recipeapp.dto.request.RegisterDTO;
import dev.pjatk.recipeapp.service.EmailService;
import dev.pjatk.recipeapp.usecase.exception.EmailAlreadyUsedException;
import dev.pjatk.recipeapp.usecase.exception.TooWeakPasswordException;
import dev.pjatk.recipeapp.usecase.user.CreateUserUseCase;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RegisterUseCase implements Loggable {
    private final CreateUserUseCase createUserUseCase;
    private final EmailService emailService;


    public void execute(RegisterDTO registerDTO) {
        try {
            log().info("Registering user with email: {}", registerDTO.email());
            checkPassword(registerDTO.password());
            var user = createUserUseCase.execute(registerDTO.email(), registerDTO.password());
            log().info("User registered: {}", user.getEmail());
            log().info("Activation token: {}", user.getActivationToken());
            emailService.senActivationEmail(user); // TODO: might be async in the future
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
