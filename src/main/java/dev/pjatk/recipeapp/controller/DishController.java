package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.DishDTO;
import dev.pjatk.recipeapp.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(DishController.DISH)
@RequiredArgsConstructor
public class DishController {
    protected static final String DISH = "/api/v1/dish";

    private final DishService dishService;

    @GetMapping
    public List<DishDTO> getAll() {
        return dishService.getAll();
    }
}
