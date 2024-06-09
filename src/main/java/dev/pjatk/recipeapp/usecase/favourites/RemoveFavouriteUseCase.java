package dev.pjatk.recipeapp.usecase.favourites;

import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveFavouriteUseCase {
    private final UserRepository userRepository;

    public void execute(Long recipeId) {
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneWithFavouriteRecipesByEmail)
                .ifPresent(user -> {
                    if (user.getFavouriteRecipes()
                            .removeIf(recipe -> recipe.getId().equals(recipeId))) {
                        userRepository.save(user);
                    }
                });
    }
}
