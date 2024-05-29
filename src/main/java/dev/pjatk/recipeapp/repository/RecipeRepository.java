package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    List<Recipe> findAllByPromotedTrue();

    // This way we omit auditing
    @Modifying
    @Query("update Recipe r set r.promoted = ?1 where r.id = ?2")
    void setPromotedForRecipe(Boolean promoted, Long id);
}