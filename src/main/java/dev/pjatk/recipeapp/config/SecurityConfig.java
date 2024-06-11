package dev.pjatk.recipeapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

import static dev.pjatk.recipeapp.util.security.Authorities.ADMIN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${recipe.allowed-origins}")
    List<String> allowedOrigins;

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
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedMethods(List.of(
                            "GET", "POST", "PUT", "DELETE", "OPTIONS"
                    ));
                    corsConfiguration.setAllowedOrigins(allowedOrigins);
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    return corsConfiguration;
                }))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self'"))
                        .referrerPolicy(referrerPolicy -> referrerPolicy
                                .policy(STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ALLOWED_STATIC_PATHS).permitAll()
                        .requestMatchers(mvc.pattern("/api/v1/register")).permitAll()
                        .requestMatchers(mvc.pattern("/api/v1/login")).permitAll()
                        .requestMatchers(mvc.pattern("/api/v1/activate")).permitAll()
                        .requestMatchers(mvc.pattern(GET, "/api/v1/tag")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/tag")).hasAnyAuthority(ADMIN)
                        .requestMatchers(mvc.pattern(GET, "/api/v1/diet")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/diet")).hasAnyAuthority(ADMIN)
                        .requestMatchers(mvc.pattern(GET, "/api/v1/dish")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/dish")).hasAnyAuthority(ADMIN)
                        .requestMatchers(mvc.pattern(GET, "/api/v1/recipe/**")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/promote/**")).hasAnyAuthority(ADMIN)
                        .requestMatchers(mvc.pattern(DELETE, "/api/v1/promote/**")).hasAnyAuthority(ADMIN)
                        .requestMatchers(mvc.pattern(GET, "/api/v1/promote/**")).permitAll()
                        .requestMatchers(mvc.pattern(GET, "/api/v1/promote")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/recipe/**")).authenticated()
                        .requestMatchers(mvc.pattern(PUT, "/api/v1/recipe/**")).authenticated()
                        .requestMatchers(mvc.pattern(GET, "/api/v1/images/**")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/images/**")).authenticated()
                        .requestMatchers(mvc.pattern(PUT, "/api/v1/images/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/v1/favourites")).authenticated()
                        .requestMatchers(mvc.pattern(GET, "/api/v1/user/**")).permitAll()
                        .requestMatchers(mvc.pattern(GET, "/api/v1/comment")).permitAll()
                        .requestMatchers(mvc.pattern(GET, "/api/v1/comment/**")).permitAll()
                        .requestMatchers(mvc.pattern(POST, "/api/v1/comment/**")).authenticated()
                        .requestMatchers(mvc.pattern(DELETE, "/api/v1/comment/**")).authenticated()
                        .requestMatchers(mvc.pattern(PUT, "/api/v1/comment/**")).authenticated()
                        .requestMatchers(mvc.pattern("/api/v1/account")).authenticated()
                        .requestMatchers(mvc.pattern("/api/v1/**")).authenticated()
                        .requestMatchers(mvc.pattern("/error")).permitAll()
                )
                .exceptionHandling(eH -> eH.authenticationEntryPoint(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginProcessingUrl("/api/v1/login")
                        .successHandler(successHandler())
                        .failureHandler(unathorizedHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler(
                                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)
                        )
                        .permitAll()
                )
                .build();
    }
}
