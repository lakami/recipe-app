package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.service.ForbiddenModificationException;
import dev.pjatk.recipeapp.service.RecipeService;
import dev.pjatk.recipeapp.usecase.AddRecipeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(RecipeController.RECIPE)
public class RecipeController {
    protected static final String RECIPE = "/api/v1/recipe";

    private final RecipeService recipeService;
    private final AddRecipeUseCase addRecipeUseCase;

    @GetMapping
    public Page<RecipeDTO> getAllRecipes(Pageable pageable) {
        return recipeService.getAllRecipesForPageable(pageable);
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipeById(Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    @RequestMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRecipe(@Valid @RequestPart("recipe") NewRecipeDTO recipeDTO,
                             @RequestParam("image") MultipartFile image) {
        return addRecipeUseCase.addRecipe(recipeDTO, image);
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
        recipeService.updateRecipe(id, recipeDTO);
    }
}
