package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewCommentDTO;
import dev.pjatk.recipeapp.dto.response.CommentDTO;
import dev.pjatk.recipeapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(CommentController.COMMENT_PATH)
public class CommentController {
    protected static final String COMMENT_PATH = "/api/v1/comment";
    private final CommentService commentService;

    @GetMapping("/{id}")
    public List<CommentDTO> getCommentsForRecipe(@PathVariable Long id) {
        return commentService.getCommentsForRecipe(id);
    }

    @PostMapping("/{id}")
    public void addComment(@PathVariable Long id, @RequestBody NewCommentDTO commentDTO) {
        commentService.addComment(id, commentDTO);
    }

    @DeleteMapping("/{recipeId}/{commentId}")
    public void deleteComment(@PathVariable Long recipeId, @PathVariable Long commentId) {
        commentService.deleteComment(recipeId, commentId);
    }

    @PutMapping("/{recipeId}/{commentId}")
    public void updateComment(@PathVariable Long recipeId,
                              @PathVariable Long commentId,
                              @RequestBody NewCommentDTO commentDTO) {
        commentService.updateComment(recipeId, commentId, commentDTO);
    }


}
