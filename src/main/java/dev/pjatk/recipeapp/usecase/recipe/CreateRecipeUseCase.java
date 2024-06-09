package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.entity.recipe.*;
import dev.pjatk.recipeapp.repository.CategoryRepository;
import dev.pjatk.recipeapp.repository.DishRepository;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateRecipeUseCase {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;

    @Transactional
    public Recipe execute(NewRecipeDTO dto, String imageId) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.name());
        recipe.setImageUrl(imageId);
        recipe.setDescription(dto.description());
        recipe.setPreparationTime(dto.preparationTime());
        recipe.setServings(dto.servings());
        recipe.setCategories(getCategories(dto.diets()));
        recipe.setTags(getTags(dto.tags()));
        recipe.setDishes(getDishes(dto.dishes()));
        recipe.setPromoted(false);
        Recipe saved = recipeRepository.save(recipe);
        Set<Step> steps = getSteps(dto.steps(), saved);
        recipe.setSteps(steps);
        Set<Ingredient> ingredients = getIngredients(dto.ingredients(), saved);
        recipe.setIngredients(ingredients);
        saved = recipeRepository.save(recipe);
        return saved;
    }

    private Set<Ingredient> getIngredients(List<String> ingredients, Recipe recipe) {
        return ingredients.stream()
                .map(text -> {
                    var ingredient = new Ingredient();
                    ingredient.setName(text);
                    ingredient.setRecipe(recipe);
                    return ingredient;
                }).collect(Collectors.toSet());
    }

    private Set<Step> getSteps(List<String> steps, Recipe recipe) {
        var set = new HashSet<Step>();
        for (int i = 0; i < steps.size(); i++) {
            var step = new Step();
            step.setDescription(steps.get(i));
            step.setNumber(i + 1);
            step.setRecipe(recipe);
            set.add(step);
        }
        return set;
    }

    private Set<Category> getCategories(Collection<String> categories) {
        return categoryRepository.findByNameIn(categories);
    }

    private Set<Dish> getDishes(Collection<String> dishes) {
        return dishRepository.findByNameIn(dishes);
    }

    private Set<Tag> getTags(Collection<String> tags) {
        return tagRepository.findByNameIn(tags);
    }
}
