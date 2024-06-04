package dev.pjatk.recipeapp.dto.response;

import java.util.List;

public record AccountDTO(
        String email,
        String firstName,
        String lastName,
        String imageUrl,
        String profileUrl,
        List<String> authorities
) {
}
