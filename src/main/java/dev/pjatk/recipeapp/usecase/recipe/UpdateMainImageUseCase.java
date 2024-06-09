package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import dev.pjatk.recipeapp.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class UpdateMainImageUseCase {

    private final ImageService imageService;
    private final RecipeRepository recipeRepository;

    @Transactional
    public void execute(Long id, MultipartFile image) {
        var recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        var oldImage = recipe.getImageUrl();
        var newImage = imageService.saveImage(image);
        recipe.setImageUrl(newImage);
        imageService.removeImage(oldImage);
        recipeRepository.save(recipe);
    }
}
