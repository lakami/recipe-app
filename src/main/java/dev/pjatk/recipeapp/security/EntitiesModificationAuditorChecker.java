package dev.pjatk.recipeapp.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security. This class is used by JPA to automatically fill the
 * createdBy and lastModifiedBy fields of entities with the current user, so that we can track who created or modified
 * an entity and when. No need to manually set these fields in the entities. Less boilerplate code. \o/
 */
@Component(value = EntitiesModificationAuditorChecker.AUDITOR_CHECKER)
public class EntitiesModificationAuditorChecker implements AuditorAware<String> {

    /**
     * If no user is authenticated, we assume that the system is the current auditor!
     */
    private static final String SYSTEM = "system";
    public static final String AUDITOR_CHECKER = "auditorChecker";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(SYSTEM));
    }
}
