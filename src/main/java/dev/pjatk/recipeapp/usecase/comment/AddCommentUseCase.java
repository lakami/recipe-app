package dev.pjatk.recipeapp.usecase.comment;

import dev.pjatk.recipeapp.dto.request.NewCommentDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.CommentRepository;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class AddCommentUseCase {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public void execute(Long recipeId, NewCommentDTO commentDTO) {
        var recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        var comment = commentDTO.toEntity();
        comment.setRecipe(recipe);
        commentRepository.save(comment);
    }

}
