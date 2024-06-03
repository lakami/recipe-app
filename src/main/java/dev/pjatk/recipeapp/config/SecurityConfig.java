package dev.pjatk.recipeapp.config;

import dev.pjatk.recipeapp.security.Authorities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final String[] ALLOWED_STATIC_PATHS = {
            "/*.js",
            "/*.css",
            "/*.svg",
            "/*.png",
            "*.ico",
            "/index.html",
            "/"
    };

    /**
     * Used to encode passwords. This bean is used automatically by Spring Security.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Used to match requests to the appropriate handler by security filter chain.
     */
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    private static AuthenticationFailureHandler unathorizedHandler() {
        return (request, response, exception) -> response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private static AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> response.setStatus(HttpStatus.OK.value());
    }

    /**
     * Configures the security filter chain that carries the responsibility of protecting our application.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                // TODO: let it stay for now this way
                .csrf(csrf -> csrf.disable()
//                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
//                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                // TODO: evaluate what is needed
//                .headers(headers -> headers
//                         .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
//                         .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'"))
//                         .referrerPolicy(referrerPolicy -> referrerPolicy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
//                )
                .authorizeHttpRequests(auth -> auth // TODO: add more rules for certain paths
                        .requestMatchers(ALLOWED_STATIC_PATHS).permitAll()
                        .requestMatchers(mvc.pattern("/api/v1/register")).permitAll()
                        .requestMatchers(mvc.pattern("/api/v1/login")).permitAll()
                        .requestMatchers(mvc.pattern("/api/v1/activate")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/tag")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/tag")).hasAnyAuthority(Authorities.ADMIN)
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/diet")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST,
                                                     "/api/v1/diet")).hasAnyAuthority(Authorities.ADMIN)
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/dish")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST,
                                                     "/api/v1/dish")).hasAnyAuthority(Authorities.ADMIN)
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/recipe/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/recipe/promote/**")).hasAnyAuthority(
                                Authorities.ADMIN)
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/v1/recipe/promote/**")).hasAnyAuthority(
                                Authorities.ADMIN)
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/recipe/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/v1/recipe/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/images/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/images/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/v1/images/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/v1/favourites")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/user/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.GET, "/api/v1/comment/**")).permitAll()
                        .requestMatchers(mvc.pattern(HttpMethod.POST, "/api/v1/comment/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.DELETE, "/api/v1/comment/**")).authenticated()
                        .requestMatchers(mvc.pattern(HttpMethod.PUT, "/api/v1/comment/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/v1/**")).authenticated()
                        .requestMatchers(mvc.pattern("/error")).permitAll()
                )
                .exceptionHandling(eH -> eH.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/api/v1/login")
                        .successHandler(successHandler())
                        .failureHandler(unathorizedHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                        .permitAll()
                )
                .build(); // TODO: rememberMe
    }
}
