package dev.pjatk.recipeapp.usecase;

import dev.pjatk.recipeapp.util.Loggable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class AddRecipeUseCase implements Loggable {

    private final Path root;

    public AddRecipeUseCase(@Value("${recipe.app-dir}") String root) {
        this.root = Path.of(root);
    }
}
