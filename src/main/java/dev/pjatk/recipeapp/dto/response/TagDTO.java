package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.recipe.Tag;

public record TagDTO(Long id, String name) {
    public TagDTO(Tag tag) {
        this(tag.getId(), tag.getName());
    }
}
