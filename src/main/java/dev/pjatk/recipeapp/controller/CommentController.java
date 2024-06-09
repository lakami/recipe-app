package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewCommentDTO;
import dev.pjatk.recipeapp.dto.response.CommentDTO;
import dev.pjatk.recipeapp.usecase.comment.AddCommentUseCase;
import dev.pjatk.recipeapp.usecase.comment.DeleteCommentUseCase;
import dev.pjatk.recipeapp.usecase.comment.GetRecipeCommentsUseCase;
import dev.pjatk.recipeapp.usecase.comment.UpdateCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(CommentController.COMMENT_PATH)
public class CommentController {
    protected static final String COMMENT_PATH = "/api/v1/comment";
    private final GetRecipeCommentsUseCase getRecipeCommentsUseCase;
    private final AddCommentUseCase addCommentUseCase;
    private final DeleteCommentUseCase deleteCommentUseCase;
    private final UpdateCommentUseCase updateCommentUseCase;

    @GetMapping("/{id}")
    public List<CommentDTO> getCommentsForRecipe(@PathVariable Long id) {
        return getRecipeCommentsUseCase.execute(id);
    }

    @PostMapping("/{id}")
    public void addComment(@PathVariable Long id, @RequestBody NewCommentDTO commentDTO) {
        addCommentUseCase.execute(id, commentDTO);
    }

    @DeleteMapping("/{recipeId}/{commentId}")
    public void deleteComment(@PathVariable Long recipeId, @PathVariable Long commentId) {
        deleteCommentUseCase.execute(recipeId, commentId);
    }

    @PutMapping("/{recipeId}/{commentId}")
    public void updateComment(@PathVariable Long recipeId,
                              @PathVariable Long commentId,
                              @RequestBody NewCommentDTO commentDTO) {
        updateCommentUseCase.execute(recipeId, commentId, commentDTO);
    }


}
