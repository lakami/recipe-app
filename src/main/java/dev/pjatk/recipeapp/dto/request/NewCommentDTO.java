package dev.pjatk.recipeapp.dto.request;

import dev.pjatk.recipeapp.entity.Comment;

public record NewCommentDTO(
        String content
) {
    public Comment toEntity() {
        var comment = new Comment();
        comment.setContent(content);
        return comment;
    }
}
