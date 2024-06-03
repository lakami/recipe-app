package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewRecipeDTO(
        @Size(min = 1, max = 100) String name,
        @Size(min = 1, max = 1000) String description,
        @Size(min = 1, max = 10000) Integer preparationTime,
        @Size(min = 1, max = 100) Integer servings,
        @NotEmpty List<String> categories,
        @NotEmpty List<String> tags,
        @NotEmpty List<String> dishes,
        @NotEmpty List<String> steps,
        @NotEmpty List<String> ingredients
) {
}
