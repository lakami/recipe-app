package dev.pjatk.recipeapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static dev.pjatk.recipeapp.TestConfig.POSTGRES_CONTAINER_NAME;
import static dev.pjatk.recipeapp.TestConfig.REDIS_CONTAINER_NAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringIntegrationTest
class RecipeAppApplicationIT {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		var postgres = context.getBean(POSTGRES_CONTAINER_NAME, PostgreSQLContainer.class);
		var redis = context.getBean(REDIS_CONTAINER_NAME, GenericContainer.class);
		assertThat(postgres.isCreated()).isTrue();
		assertThat(postgres.isRunning()).isTrue();
		assertThat(redis.isCreated()).isTrue();
		assertThat(redis.isRunning()).isTrue();
	}
}
