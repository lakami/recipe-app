package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.recipe.Dish;

public record DishDTO(Long id, String name) {
    public DishDTO(Dish dish) {
        this(dish.getId(), dish.getName());
    }
}
