package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.exception.ForbiddenModificationException;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.usecase.recipe.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(RecipeController.RECIPE)
public class RecipeController {
    protected static final String RECIPE = "/api/v1/recipe";

    private final AddRecipeUseCase addRecipeUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final GetRecipeByIdUseCase getRecipeByIdUseCase;
    private final FindRecipesUseCase findRecipesUseCase;
    private final UpdateRecipeImageUseCase updateRecipeImageUseCase;

    @GetMapping
    public Page<RecipeDTO> getAllRecipes(Pageable pageable,
                                         @RequestParam(required = false) List<String> diets,
                                         @RequestParam(required = false) List<String> dishes,
                                         @RequestParam(required = false) List<String> tags,
                                         @RequestParam(required = false) String search) {
        return findRecipesUseCase.execute(pageable, diets, dishes, tags, search);
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipeById(@PathVariable Long id) {
        return getRecipeByIdUseCase.execute(id);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRecipe(@Valid @RequestPart("recipe") NewRecipeDTO recipe,
                             @RequestParam("image") MultipartFile image) {
        return addRecipeUseCase.execute(recipe, image);
    }

    /**
     * @param id        recipe id
     * @param recipeDTO recipe data
     * @throws ResourceNotFoundException      if recipe with id not found
     * @throws ForbiddenModificationException if permission denied (recipe is not owned by user or user is not an admin)
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipe(@PathVariable Long id, @Valid @RequestBody NewRecipeDTO recipeDTO) {
        updateRecipeUseCase.execute(id, recipeDTO);
    }

    /**
     * @param id    recipe id
     * @param image new image
     * @throws ResourceNotFoundException      if recipe with id not found
     * @throws ForbiddenModificationException if permission denied (recipe is not owned by user or user is not an admin)
     */
    @PutMapping("/{id}/image")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipeImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        updateRecipeImageUseCase.execute(id, image);
    }
}
