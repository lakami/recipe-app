package dev.pjatk.recipeapp.usecase.favourites;

import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.entity.recipe.Recipe;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddRecipeToFavouritesUseCase {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public void execute(Long recipeId) {
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneWithFavouriteRecipesByEmail)
                .map(user -> recipeRepository.findById(recipeId)
                        .map(recipe -> new UserAndRecipe(user, recipe))
                        .orElseThrow(() -> new ResourceNotFoundException("Recipe not found")))
                .ifPresent(userAndRecipe -> {
                    User user = userAndRecipe.user;
                    Recipe recipe = userAndRecipe.recipe;
                    if (user.getFavouriteRecipes().contains(recipe)) {
                        return;
                    }
                    user.getFavouriteRecipes().add(recipe);
                    userRepository.save(user);
                });
    }

    private record UserAndRecipe(User user, Recipe recipe) {
    }
}
