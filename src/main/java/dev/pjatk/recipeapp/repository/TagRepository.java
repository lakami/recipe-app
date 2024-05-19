package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByOrderByNameAsc();
}