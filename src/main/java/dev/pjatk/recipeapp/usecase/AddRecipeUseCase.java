package dev.pjatk.recipeapp.usecase;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.service.ImageService;
import dev.pjatk.recipeapp.service.RecipeService;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AddRecipeUseCase implements Loggable {

    private final RecipeService recipeService;
    private final ImageService imageService;

    /**
     * @param recipeDTO recipa data
     * @param image     image file
     * @return created recipe id
     * @throws RuntimeException if image cannot be saved
     */
    public Long addRecipe(NewRecipeDTO recipeDTO, MultipartFile image) {
        log().info("Adding recipe with name: {}", recipeDTO.name());
        var imageId = imageService.saveImage(image);
        var recipe = recipeService.addRecipe(recipeDTO, imageId);
        return recipe.getId();
    }
}
