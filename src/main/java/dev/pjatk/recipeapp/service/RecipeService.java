package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.dto.response.*;
import dev.pjatk.recipeapp.entity.recipe.Category;
import dev.pjatk.recipeapp.entity.recipe.Dish;
import dev.pjatk.recipeapp.entity.recipe.Recipe;
import dev.pjatk.recipeapp.entity.recipe.Tag;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.CategoryRepository;
import dev.pjatk.recipeapp.repository.DishRepository;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.repository.TagRepository;
import dev.pjatk.recipeapp.security.Authorities;
import dev.pjatk.recipeapp.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;

    private static boolean isAdmin() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities(Authorities.ADMIN);
    }

    public RecipeDTO getRecipeById(Long id) {
        return recipeRepository
                .findById(id)
                .map(RecipeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }

    public Page<RecipeDTO> getAllRecipesForPageable(Pageable pageable) {
        return recipeRepository
                .findAll(pageable)
                .map(RecipeDTO::new);
    }

    @Transactional
    public Recipe addRecipe(NewRecipeDTO dto, String imageId) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.name());
        recipe.setImageUrl(imageId);
        recipe.setDescription(dto.description());
        recipe.setPreparationTime(dto.preparationTime());
        recipe.setServings(dto.servings());
        recipe.setCategories(getCategories(dto.categories()));
        recipe.setTags(getTags(dto.tags()));
        recipe.setDishes(getDishes(dto.dishes()));
        recipe.setSteps(dto.steps().stream().map(StepDTO::toEntity).collect(Collectors.toSet()));
        recipe.setIngredients(dto.ingredients().stream().map(IngredientDTO::toEntity).collect(Collectors.toSet()));
        return recipeRepository.save(recipe);
    }

    private Set<Category> getCategories(Collection<CategoryDTO> categories) {
        return categoryRepository.findByNameIn(categories.stream().map(CategoryDTO::name).toList());
    }

    private Set<Dish> getDishes(Collection<DishDTO> dishes) {
        return dishRepository.findByNameIn(dishes.stream().map(DishDTO::name).toList());
    }

    private Set<Tag> getTags(Collection<TagDTO> tags) {
        return tagRepository.findByNameIn(tags.stream().map(TagDTO::name).toList());
    }

    /**
     * Update recipe. Remember to remove any changed sub-entities.
     * Only author and admin can update recipe.
     *
     * @param id        existing recipe id
     * @param recipeDTO new recipe data
     */
    public void updateRecipe(Long id, NewRecipeDTO recipeDTO) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        if (!(isAdmin() || isAuthor(recipe))) {
            throw new ForbiddenModificationException();
        }
        recipe.setName(recipeDTO.name());
        recipe.setDescription(recipeDTO.description());
        recipe.setPreparationTime(recipeDTO.preparationTime());
        recipe.setServings(recipeDTO.servings());
        recipe.setCategories(getCategories(recipeDTO.categories()));
        recipe.setTags(getTags(recipeDTO.tags()));
        recipe.setDishes(getDishes(recipeDTO.dishes()));
        recipe.setSteps(recipeDTO.steps().stream().map(StepDTO::toEntity).collect(Collectors.toSet()));
        recipe.setIngredients(recipeDTO.ingredients().stream().map(IngredientDTO::toEntity).collect(Collectors.toSet()));
        recipeRepository.save(recipe);
    }

    private boolean isAuthor(Recipe recipe) {
        var currentUser = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return recipe.getCreatedBy().getEmail().equals(currentUser);
    }
}
