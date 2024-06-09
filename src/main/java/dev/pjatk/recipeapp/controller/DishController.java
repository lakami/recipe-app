package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewDishDTO;
import dev.pjatk.recipeapp.dto.response.DishDTO;
import dev.pjatk.recipeapp.usecase.dish.CreateDishUseCase;
import dev.pjatk.recipeapp.usecase.dish.GetAllDishesUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DishController.DISH)
@RequiredArgsConstructor
public class DishController {
    protected static final String DISH = "/api/v1/dish";
    private final GetAllDishesUseCase getAllDishesUseCase;
    private final CreateDishUseCase createDishUseCase;


    @GetMapping
    public List<DishDTO> getAll() {
        return getAllDishesUseCase.execute();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DishDTO create(@Valid @RequestBody NewDishDTO dishDTO) {
        return createDishUseCase.execute(dishDTO);
    }
}
