package dev.pjatk.recipeapp.security;

import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security. This class is used by JPA to automatically fill the
 * createdBy and lastModifiedBy fields of entities with the current user, so that we can track who created or modified
 * an entity and when. No need to manually set these fields in the entities. Less boilerplate code. \o/
 */
@Component(value = EntitiesModificationAuditorChecker.AUDITOR_CHECKER)
@RequiredArgsConstructor
public class EntitiesModificationAuditorChecker
        implements Loggable, AuditorAware<User>, ApplicationListener<ContextRefreshedEvent> {

    /**
     * If no user is authenticated, we assume that the system is the current auditor!
     */
    private static final Long ADMIN = 1L;
    public static final String AUDITOR_CHECKER = "auditorChecker";

    private final UserRepository userRepository;
    private User systemUser;

    @Override
    public Optional<User> getCurrentAuditor() {
        return SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findByEmail)
                .or(() -> Optional.of(systemUser)); // no user authenticated, then system is the auditor
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (this.systemUser == null) {
            log().info("Setting system user as auditor");
            systemUser = userRepository.findById(ADMIN).orElseThrow();
        }
    }
}
