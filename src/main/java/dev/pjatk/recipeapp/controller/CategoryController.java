package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.CategoryDTO;
import dev.pjatk.recipeapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(CategoryController.CATEGORY)
@RequiredArgsConstructor
public class CategoryController {
    protected static final String CATEGORY = "/api/v1/category";

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAll();
    }
}
