package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRecipeByIdUseCase {

    private final RecipeRepository recipeRepository;

    public RecipeDTO execute(Long id) {
        return recipeRepository
                .findById(id)
                .map(RecipeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }
}
