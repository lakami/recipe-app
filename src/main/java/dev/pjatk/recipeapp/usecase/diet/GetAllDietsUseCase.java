package dev.pjatk.recipeapp.usecase.diet;

import dev.pjatk.recipeapp.dto.response.CategoryDTO;
import dev.pjatk.recipeapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GetAllDietsUseCase {
    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> execute() {
        return categoryRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .toList();
    }
}
