package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewCategoryDTO;
import dev.pjatk.recipeapp.dto.response.CategoryDTO;
import dev.pjatk.recipeapp.usecase.diet.CreateDietUseCase;
import dev.pjatk.recipeapp.usecase.diet.GetAllDietsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DietController.CATEGORY)
@RequiredArgsConstructor
public class DietController {
    protected static final String CATEGORY = "/api/v1/diet";

    private final GetAllDietsUseCase getAllDietsUseCase;
    private final CreateDietUseCase createDietUseCase;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return getAllDietsUseCase.execute();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@Valid @RequestBody NewCategoryDTO categoryDTO) {
        return createDietUseCase.execute(categoryDTO);
    }
}
