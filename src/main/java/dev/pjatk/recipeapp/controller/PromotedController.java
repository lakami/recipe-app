package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.usecase.promote.DemoteRecipeUseCase;
import dev.pjatk.recipeapp.usecase.promote.GetPromotedRecipesUseCase;
import dev.pjatk.recipeapp.usecase.promote.PromoteRecipeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(PromotedController.PROMOTED)
public class PromotedController {
    protected static final String PROMOTED = "/api/v1/promoted";
    private final PromoteRecipeUseCase promoteRecipeUseCase;
    private final DemoteRecipeUseCase demoteRecipeUseCase;
    private final GetPromotedRecipesUseCase getPromotedRecipesUseCase;

    @GetMapping()
    public List<RecipeDTO> getPromotedRecipes() {
        return getPromotedRecipesUseCase.execute();
    }

    @GetMapping("/{id}")
    public boolean isPromoted(@PathVariable Long id) {
        return getPromotedRecipesUseCase.execute().stream().anyMatch(recipe -> recipe.id().equals(id));
    }

    @PostMapping("/{id}")
    public void promoteRecipe(@PathVariable Long id) {
        promoteRecipeUseCase.execute(id);
    }

    @DeleteMapping("/{id}")
    public void demoteRecipe(@PathVariable Long id) {
        demoteRecipeUseCase.execute(id);
    }
}
