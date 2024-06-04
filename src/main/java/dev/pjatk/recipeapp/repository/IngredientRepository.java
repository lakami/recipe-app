package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}