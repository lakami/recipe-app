package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.recipe.Ingredient;

public record IngredientDTO(String name) {
    public IngredientDTO(Ingredient ingredient) {
        this(ingredient.getName());
    }

    public static Ingredient toEntity(IngredientDTO ingredientDTO) {
        var ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.name());
        return ingredient;
    }
}
