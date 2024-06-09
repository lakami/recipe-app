package dev.pjatk.recipeapp.usecase.comment;

import dev.pjatk.recipeapp.exception.ForbiddenModificationException;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.CommentRepository;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DeleteCommentUseCase {
    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public void execute(Long recipeId, Long commentId) {
        var recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException(
                "Recipe not found"));
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException(
                "Comment not found"));

        if (!SecurityUtils.isAdminOrCurrentUser(comment.getCreatedBy().getEmail())) {
            throw new ForbiddenModificationException();
        }

        if (!recipe.getComments().contains(comment)) {
            throw new ResourceNotFoundException("Comment not found in recipe");
        }

        recipe.getComments().remove(comment);
        recipeRepository.save(recipe);
    }
}
