package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.recipe.Step;

public record StepDTO(int number, String description) {
    public StepDTO(Step step) {
        this(step.getNumber(), step.getDescription());
    }
}
