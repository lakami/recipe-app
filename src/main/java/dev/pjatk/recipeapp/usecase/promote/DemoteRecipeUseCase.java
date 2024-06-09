package dev.pjatk.recipeapp.usecase.promote;

import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class DemoteRecipeUseCase {
    private final RecipeRepository recipeRepository;

    @Transactional
    public void execute(Long id) {
        recipeRepository.setPromotedForRecipe(false, id);
    }
}
