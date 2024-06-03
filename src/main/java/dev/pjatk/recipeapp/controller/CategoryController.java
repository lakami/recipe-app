package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewCategoryDTO;
import dev.pjatk.recipeapp.dto.response.CategoryDTO;
import dev.pjatk.recipeapp.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(CategoryController.CATEGORY)
@RequiredArgsConstructor
public class CategoryController {
    protected static final String CATEGORY = "/api/v1/diet";

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@Valid @RequestBody NewCategoryDTO categoryDTO) {
        return categoryService.create(categoryDTO);
    }
}
