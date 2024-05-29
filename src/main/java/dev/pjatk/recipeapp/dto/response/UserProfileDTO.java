package dev.pjatk.recipeapp.dto.response;

import java.util.Set;

public record UserProfileDTO(
        String firstName,
        String lastName,
        String profileUrl,
        String imageUrl,
        Set<RecipeDTO> authoredRecipes
) {
}
