package dev.pjatk.recipeapp.usecase.user;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.dto.response.UserProfileDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Transactional
public class GetUserProfileUseCase {
    private final UserRepository userRepository;

    public UserProfileDTO execute(String profileUrl) {
        return userRepository.findOneWithRecipesByProfileUrl(profileUrl)
                .map(user -> {
                    user.getRecipes().size(); // force fetch
                    return user;
                })
                .map(user -> new UserProfileDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getProfileUrl(),
                        user.getImageUrl(),
                        user.getRecipes()
                                .stream()
                                .map(RecipeDTO::new)
                                .collect(Collectors.toSet())))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
