package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByOrderByNameAsc();

    Set<Tag> findByNameIn(Collection<String> names);
}