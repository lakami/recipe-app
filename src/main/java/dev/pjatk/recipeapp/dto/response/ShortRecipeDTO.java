package dev.pjatk.recipeapp.dto.response;

public record ShortRecipeDTO(
        Long id,
        String name,
        String imageUrl
        // TODO: Add more fields if needed
) {
}
