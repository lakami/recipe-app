package dev.pjatk.recipeapp.usecase.diet;


import dev.pjatk.recipeapp.dto.request.NewCategoryDTO;
import dev.pjatk.recipeapp.dto.response.CategoryDTO;
import dev.pjatk.recipeapp.entity.recipe.Category;
import dev.pjatk.recipeapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateDietUseCase {
    private final CategoryRepository categoryRepository;

    public CategoryDTO execute(NewCategoryDTO newCategoryDTO) {
        var category = new Category();
        category.setName(newCategoryDTO.name());
        categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getName());
    }
}
