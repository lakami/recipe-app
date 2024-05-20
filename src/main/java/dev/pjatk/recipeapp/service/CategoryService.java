package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.response.CategoryDTO;
import dev.pjatk.recipeapp.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAll() {
        return categoryRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .toList();
    }
}