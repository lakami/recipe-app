package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.entity.recipe.*;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.*;
import dev.pjatk.recipeapp.security.Authorities;
import dev.pjatk.recipeapp.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;
    private final StepRepository stepRepository;

    private static boolean isAdmin() {
        return SecurityUtils.hasCurrentUserAnyOfAuthorities(Authorities.ADMIN);
    }

    public RecipeDTO getRecipeById(Long id) {
        return recipeRepository
                .findById(id)
                .map(RecipeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }

    public Page<RecipeDTO> findAllMatchingRecipesForPage(Pageable pageable,
                                                         List<String> categories,
                                                         List<String> dishes,
                                                         List<String> tags,
                                                         String search) {
        List<Specification<Recipe>> specifications = new LinkedList<>();
        if (categories != null && !categories.isEmpty()) {
            var spec = (Specification<Recipe>) (root, query, criteriaBuilder) -> root.join("categories").get("name").in(
                    categories);
            specifications.add(spec);
        }

        if (dishes != null && !dishes.isEmpty()) {
            var spec = (Specification<Recipe>) (root, query, criteriaBuilder) -> root.join("dishes").get("name").in(
                    dishes);
            specifications.add(spec);
        }

        if (tags != null && !tags.isEmpty()) {
            var spec = (Specification<Recipe>) (root, query, criteriaBuilder) -> root.join("tags").get("name").in(tags);
            specifications.add(spec);
        }

        if (search != null && !search.isBlank()) {
            var specName = (Specification<Recipe>) (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), "%" + search.toLowerCase() + "%");
            var specDesc = (Specification<Recipe>) (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), "%" + search.toLowerCase() + "%");

            var spec = specName.or(specDesc);
            specifications.add(spec);
        }

        var spec = Specification.allOf(specifications);

        return recipeRepository
                .findAll(spec, pageable)
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

    /**
     * Update recipe. Remember to remove any changed sub-entities.
     * Only author and admin can update recipe.
     *
     * @param id        existing recipe id
     * @param recipeDTO new recipe data
     */
    @Transactional
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
        recipe.setCategories(getCategories(recipeDTO.diets()));
        recipe.setTags(getTags(recipeDTO.tags()));
        recipe.setDishes(getDishes(recipeDTO.dishes()));
        // TODO: update steps and ingredients
//        recipe.setSteps(recipeDTO.steps().stream().map(StepDTO::toEntity).collect(Collectors.toSet()));
//        recipe.setIngredients(recipeDTO.ingredients().stream().map(IngredientDTO::toEntity).collect(Collectors.toSet()));
        recipeRepository.save(recipe);
    }

    private boolean isAuthor(Recipe recipe) {
        var currentUser = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return recipe.getCreatedBy().getEmail().equals(currentUser);
    }

    public List<RecipeDTO> getPromotedRecipes() {
        return recipeRepository.findAllByPromotedTrue()
                .stream()
                .map(RecipeDTO::new)
                .toList();
    }

    @Transactional
    public void promoteRecipe(Long id) {
        recipeRepository.setPromotedForRecipe(true, id);
    }

    @Transactional
    public void demoteRecipe(Long id) {
        recipeRepository.setPromotedForRecipe(false, id);
    }
}
