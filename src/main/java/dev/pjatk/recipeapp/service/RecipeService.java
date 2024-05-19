package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public Page<RecipeDTO> getAllRecipesForPagable(Pageable pageable) {
        return recipeRepository
                .findAll(pageable)
                .map(RecipeDTO::new);
    }

    public RecipeDTO getRecipeById(Long id) {
        return recipeRepository
                .findById(id)
                .map(RecipeDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }
}
