package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.service.ImageService;
import dev.pjatk.recipeapp.util.Loggable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AddRecipeUseCase implements Loggable {

    private final CreateRecipeUseCase createRecipeUseCase;
    private final ImageService imageService;

    /**
     * @param recipeDTO recipa data
     * @param image     image file
     * @return created recipe id
     * @throws RuntimeException if image cannot be saved
     */
    public Long execute(NewRecipeDTO recipeDTO, MultipartFile image) {
        log().info("Adding recipe with name: {}", recipeDTO.name());
        var imageId = imageService.saveImage(image);
        try {
            var recipe = createRecipeUseCase.execute(recipeDTO, imageId);
            return recipe.getId();
        } catch (RuntimeException e) {
            log().error("Error while adding recipe: {}", e.getMessage());
            imageService.removeImage(imageId);
            throw e; // rethrow exception
        }
    }
}
