package dev.pjatk.recipeapp.util.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants for application authorities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Authorities {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
