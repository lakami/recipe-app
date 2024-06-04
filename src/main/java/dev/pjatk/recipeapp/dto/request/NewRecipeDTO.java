package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewRecipeDTO(
        @Size(min = 1, max = 100) String name,
        @Size(min = 1, max = 1000) String description,
        @Min(1) @Max(10000) Integer preparationTime,
        @Min(1) @Max(100) Integer servings,
        @NotEmpty List<String> diets,
        @NotEmpty List<String> tags,
        @NotEmpty List<String> dishes,
        @NotEmpty List<String> steps,
        @NotEmpty List<String> ingredients
) {
}
