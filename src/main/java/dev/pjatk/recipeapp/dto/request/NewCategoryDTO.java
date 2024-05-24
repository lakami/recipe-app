package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.Size;

public record NewCategoryDTO(@Size(min = 1, max = 100) String name) {
}
