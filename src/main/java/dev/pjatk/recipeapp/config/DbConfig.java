package dev.pjatk.recipeapp.config;

import dev.pjatk.recipeapp.security.EntitiesModificationAuditorChecker;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories({"dev.pjatk.recipeapp.repository"}) // find JPA repositories
@EntityScan("dev.pjatk.recipeapp.entity") // find JPA entities
@EnableJpaAuditing(auditorAwareRef = EntitiesModificationAuditorChecker.AUDITOR_CHECKER) // use custom auditor checker
@EnableTransactionManagement
public class DbConfig {
}
