package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.Size;

public record NewDishDTO(@Size(min = 1, max = 100) String name) {
}
