package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.entity.recipe.*;
import dev.pjatk.recipeapp.exception.ForbiddenModificationException;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.CategoryRepository;
import dev.pjatk.recipeapp.repository.DishRepository;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.repository.TagRepository;
import dev.pjatk.recipeapp.util.security.SecurityUtils;
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
public class UpdateRecipeUseCase {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;

    /**
     * Update recipe. Remember to remove any changed sub-entities.
     * Only author and admin can update recipe.
     *
     * @param id        existing recipe id
     * @param recipeDTO new recipe data
     */
    @Transactional
    public void execute(Long id, NewRecipeDTO recipeDTO) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        if (!SecurityUtils.isAdminOrCurrentUser(recipe.getCreatedBy().getEmail())) {
            throw new ForbiddenModificationException();
        }
        recipe.setName(recipeDTO.name());
        recipe.setDescription(recipeDTO.description());
        recipe.setPreparationTime(recipeDTO.preparationTime());
        recipe.setServings(recipeDTO.servings());
        recipe.setCategories(getCategories(recipeDTO.diets()));
        recipe.setTags(getTags(recipeDTO.tags()));
        recipe.setDishes(getDishes(recipeDTO.dishes()));
        recipe.getIngredients().stream().toList().forEach(ingredient -> recipe.removeIngredient(ingredient));
        recipe.getSteps().stream().toList().forEach(step -> recipe.removeStep(step));
        getIngredients(recipeDTO.ingredients(), recipe).forEach(recipe::addIngredient);
        getSteps(recipeDTO.steps(), recipe).forEach(recipe::addStep);
        recipeRepository.save(recipe);
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
