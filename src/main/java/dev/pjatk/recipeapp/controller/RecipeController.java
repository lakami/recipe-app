package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.AddRecipeDTO;
import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.service.RecipeService;
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

    @GetMapping
    public Page<RecipeDTO> getAllRecipes(Pageable pageable) {
        return recipeService.getAllRecipesForPagable(pageable);
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipeById(Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    @RequestMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRecipe(@Valid @RequestPart("recipe") AddRecipeDTO recipeDTO,
                             @RequestParam("image") MultipartFile image) {
        return 0L;
    }
}
