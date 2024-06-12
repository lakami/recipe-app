package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.exception.ForbiddenModificationException;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.service.ImageService;
import dev.pjatk.recipeapp.util.Loggable;
import dev.pjatk.recipeapp.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class UpdateRecipeImageUseCase implements Loggable {
    private final ImageService imageService;
    private final RecipeRepository recipeRepository;

    public void execute(Long recipeId, MultipartFile image) {

        var recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        if (!SecurityUtils.isAdminOrCurrentUser(recipe.getCreatedBy().getEmail())) {
            throw new ForbiddenModificationException();
        }
        boolean success;
        var currentImage = recipe.getImageUrl();
        try {
            var newImage = imageService.saveImage(image);
            recipe.setImageUrl(newImage);
            recipeRepository.save(recipe);
            success = true;
        } catch (Exception e) {
            throw new RuntimeException("Cannot save image");
        }

        // non-critical operation, do not throw exception if it fails
        if (success) {
            try {
                imageService.removeImage(currentImage);
            } catch (Exception e) {
                log().error("Cannot remove old image", e);
            }
        }


    }
}
