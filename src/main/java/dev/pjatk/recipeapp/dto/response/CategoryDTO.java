package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.recipe.Category;

public record CategoryDTO(Long id, String name) {
    public CategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}
