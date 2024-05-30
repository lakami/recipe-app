package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByRecipeIdOrderByCreatedByDesc(Long recipeId);
}
