package dev.pjatk.recipeapp.dto.request;

import dev.pjatk.recipeapp.dto.response.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public record NewRecipeDTO(
        @Size(min = 1, max = 100) String name,
        @Size(min = 1, max = 1000) String description,
        @Size(min = 1, max = 10000) Integer preparationTime,
        @Size(min = 1, max = 100) Integer servings,
        @NotEmpty Set<CategoryDTO> categories,
        @NotEmpty Set<TagDTO> tags,
        @NotEmpty Set<DishDTO> dishes,
        @NotEmpty List<StepDTO> steps,
        @NotEmpty List<IngredientDTO> ingredients
) {
}
