package dev.pjatk.recipeapp.config.exception;

import org.springframework.security.core.AuthenticationException;

public class UserInactiveException extends AuthenticationException {
    public UserInactiveException(String email) {
        super("User %s was not activated".formatted(email));
    }
}
