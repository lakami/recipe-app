package dev.pjatk.recipeapp.security;

import dev.pjatk.recipeapp.entity.Authority;
import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.util.Loggable;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Retrieve user details from the database. This component is automatically used by Spring Security, because it is
 * annotated with {@link Component} with the name "userDetailsService".
 */
@Component("userDetailsService")
public class RecipeUserDetailsService implements UserDetailsService, Loggable {
    private final UserRepository userRepository;

    public RecipeUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *
     * @param email the username identifying the user whose data is required.
     * @return the user details.
     * @throws UsernameNotFoundException when the user is not found.
     * @throws UserInactiveException when the user is not activated.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        log().debug("Obtaining user by email: {}", email);

        // no need to find user, if email is not valid, because it will not be found anyway
        if(!new EmailValidator().isValid(email, null)) {
            throw new UsernameNotFoundException("Email %s is not valid".formatted(email));
        }

        return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(email)
                .map(RecipeUserDetailsService::asSecuredUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with email %s was not found".formatted(email)));
    }

    private static org.springframework.security.core.userdetails.User asSecuredUser(User user) {
        if (!user.isActivated()) {
            throw new UserInactiveException(user.getEmail());
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private static List<SimpleGrantedAuthority> getAuthorities(User user) {
        return user.getAuthorities()
                .stream()
                .map(Authority::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
