package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByOrderByNameAsc();
}