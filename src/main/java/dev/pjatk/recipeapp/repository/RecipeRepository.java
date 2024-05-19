package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}