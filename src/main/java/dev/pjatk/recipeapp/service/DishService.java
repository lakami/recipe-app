package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.NewDishDTO;
import dev.pjatk.recipeapp.dto.response.DishDTO;
import dev.pjatk.recipeapp.entity.recipe.Dish;
import dev.pjatk.recipeapp.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DishService {

    private final DishRepository dishRepository;

    public List<DishDTO> getAll() {
        return dishRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(dish -> new DishDTO(dish.getId(), dish.getName()))
                .toList();
    }

    public DishDTO create(NewDishDTO dishDTO) {
        var dish = new Dish();
        dish.setName(dishDTO.name());
        dishRepository.save(dish);
        return new DishDTO(dish.getId(), dish.getName());
    }
}
