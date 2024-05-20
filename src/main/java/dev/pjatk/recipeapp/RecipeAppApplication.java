package dev.pjatk.recipeapp;

import dev.pjatk.recipeapp.util.Loggable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

	public static void main(String[] args) {
		SpringApplication.run(RecipeAppApplication.class, args);
	}

}
