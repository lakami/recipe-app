package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByOrderByNameAsc();

    Set<Dish> findByNameIn(Collection<String> names);
}