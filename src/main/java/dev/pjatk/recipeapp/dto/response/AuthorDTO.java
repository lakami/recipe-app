package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.User;

public record AuthorDTO(
        String firstName,
        String lastName,
        String profileUrl
) {
    public AuthorDTO(User user) {
        this(user.getFirstName(), user.getLastName(), user.getProfileUrl());
    }
}
