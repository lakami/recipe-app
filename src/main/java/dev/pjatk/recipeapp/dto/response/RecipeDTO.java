package dev.pjatk.recipeapp.dto.response;

import dev.pjatk.recipeapp.entity.recipe.Recipe;
import dev.pjatk.recipeapp.entity.recipe.Step;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public record RecipeDTO(
        Long id,
        String name,
        String description,
        String imageUrl,
        Integer servings,
        Integer preparationTime, // in minutes!
        List<IngredientDTO> ingredients,
        List<StepDTO> steps,
        List<TagDTO> tags,
        List<CategoryDTO> diets,
        List<DishDTO> dishes,
        AuthorDTO author,
        Instant createdDate
) {
    public RecipeDTO(Recipe recipe) {
        this(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getImageUrl(),
                recipe.getServings(),
                recipe.getPreparationTime(),
                recipe.getIngredients().stream().map(IngredientDTO::new).toList(),
                recipe.getSteps().stream().sorted(Comparator.comparingInt(Step::getNumber)).map(StepDTO::new).toList(),
                recipe.getTags().stream().map(TagDTO::new).toList(),
                recipe.getCategories().stream().map(CategoryDTO::new).toList(),
                recipe.getDishes().stream().map(DishDTO::new).toList(),
                new AuthorDTO(recipe.getCreatedBy()),
                recipe.getCreatedDate()
        );
    }
}
