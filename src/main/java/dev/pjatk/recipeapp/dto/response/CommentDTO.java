package dev.pjatk.recipeapp.dto.response;

import java.time.Instant;

public record CommentDTO(
        Long id,
        Long recipeId,
        String content,
        AuthorDTO createdBy,
        Instant createdDate,
        AuthorDTO lastModifiedBy,
        Instant lastModifiedDate
) {
    public CommentDTO(dev.pjatk.recipeapp.entity.Comment comment) {
        this(
                comment.getId(),
                comment.getRecipe().getId(),
                comment.getContent(),
                new AuthorDTO(comment.getCreatedBy()),
                comment.getCreatedDate(),
                new AuthorDTO(comment.getLastModifiedBy()),
                comment.getLastModifiedDate()
        );
    }
}
