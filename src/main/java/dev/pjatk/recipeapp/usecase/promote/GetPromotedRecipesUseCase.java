package dev.pjatk.recipeapp.usecase.promote;


import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GetPromotedRecipesUseCase {
    private final RecipeRepository recipeRepository;

    public List<RecipeDTO> execute() {
        return recipeRepository.findAllByPromotedTrue()
                .stream()
                .map(RecipeDTO::new)
                .toList();
    }
}
