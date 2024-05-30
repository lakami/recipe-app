package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.NewCommentDTO;
import dev.pjatk.recipeapp.dto.response.CommentDTO;
import dev.pjatk.recipeapp.entity.Comment;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.CommentRepository;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.security.Authorities;
import dev.pjatk.recipeapp.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    private static boolean isAdmin() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities(Authorities.ADMIN);
    }

    public List<CommentDTO> getCommentsForRecipe(Long id) {
        return commentRepository.findAllByRecipeIdOrderByCreatedByDesc(id)
                .stream()
                .map(CommentDTO::new)
                .toList();
    }

    @Transactional
    public void addComment(Long id, NewCommentDTO commentDTO) {
        var recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        var comment = commentDTO.toEntity();
        comment.setRecipe(recipe);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long recipeId, Long commentId) {
        var recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException(
                "Recipe not found"));
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException(
                "Comment not found"));

        if (!(isAdmin() || isAuthor(comment))) {
            throw new ForbiddenModificationException();
        }

        if (!recipe.getComments().contains(comment)) {
            throw new ResourceNotFoundException("Comment not found in recipe");
        }

        recipe.getComments().remove(comment);
        recipeRepository.save(recipe);
    }

    @Transactional
    public void updateComment(Long recipeId, Long commentId, NewCommentDTO commentDTO) {
        var recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException(
                "Recipe not found"));
        var comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException(
                "Comment not found"));

        if (!(isAdmin() || isAuthor(comment))) {
            throw new ForbiddenModificationException();
        }

        if (!recipe.getComments().contains(comment)) {
            throw new ResourceNotFoundException("Comment not found in recipe");
        }

        comment.setContent(commentDTO.content());
        commentRepository.save(comment);
    }

    private boolean isAuthor(Comment comment) {
        var currentUser = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return comment.getCreatedBy().getEmail().equals(currentUser);
    }
}
