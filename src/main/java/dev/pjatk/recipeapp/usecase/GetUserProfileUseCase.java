package dev.pjatk.recipeapp.usecase;

import dev.pjatk.recipeapp.dto.response.ShortRecipeDTO;
import dev.pjatk.recipeapp.dto.response.UserProfileDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GetUserProfileUseCase {
    private final IUserService userService;

    public UserProfileDTO getUser(String profileUrl) {
        return userService.getUserWithRecipes(profileUrl)
                .map(user -> new UserProfileDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getProfileUrl(),
                        user.getImageUrl(),
                        user.getRecipes()
                                .stream()
                                .map(recipe -> new ShortRecipeDTO(
                                        recipe.getId(),
                                        recipe.getName(),
                                        recipe.getImageUrl()
                                )).collect(Collectors.toSet())))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
