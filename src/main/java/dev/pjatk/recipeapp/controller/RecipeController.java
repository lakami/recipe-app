package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
