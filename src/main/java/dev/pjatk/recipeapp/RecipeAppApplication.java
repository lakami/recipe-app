package dev.pjatk.recipeapp;

import dev.pjatk.recipeapp.util.Loggable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@SpringBootApplication
public class RecipeAppApplication implements Loggable {

	public RecipeAppApplication(@Value("${recipe.app-dir}") String root) {
		createDirIfNotExists(root);
	}

	private void createDirIfNotExists(String root) {
		var dir = new java.io.File(root);
		if (!dir.exists()) {
			var success = dir.mkdirs();
			if (!success) {
				log().error("Failed to create directory: {}", root);
				System.exit(1);
			}
			log().info("Directory created: {}", root);
		} else {
			log().info("Directory already exists: {}", root);
		}
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(RecipeAppApplication.class, args);
	}

}
