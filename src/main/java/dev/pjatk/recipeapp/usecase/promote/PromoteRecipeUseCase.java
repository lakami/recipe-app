package dev.pjatk.recipeapp.usecase.promote;

import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PromoteRecipeUseCase {
    private final RecipeRepository recipeRepository;

    @Transactional
    public void execute(Long id) {
        recipeRepository.setPromotedForRecipe(true, id);
    }
}
