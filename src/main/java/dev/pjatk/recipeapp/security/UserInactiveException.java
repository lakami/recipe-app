package dev.pjatk.recipeapp.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.AuthenticationException;

public class UserInactiveException extends AuthenticationException {
    public UserInactiveException(String email) {
        super("User %s was not activated".formatted(email));
    }
}
