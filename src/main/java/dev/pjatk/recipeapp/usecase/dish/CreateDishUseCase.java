package dev.pjatk.recipeapp.usecase.dish;

import dev.pjatk.recipeapp.dto.request.NewDishDTO;
import dev.pjatk.recipeapp.dto.response.DishDTO;
import dev.pjatk.recipeapp.entity.recipe.Dish;
import dev.pjatk.recipeapp.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateDishUseCase {

    private final DishRepository dishRepository;

    public DishDTO execute(NewDishDTO dishDTO) {
        var dish = new Dish();
        dish.setName(dishDTO.name());
        dishRepository.save(dish);
        return new DishDTO(dish.getId(), dish.getName());
    }
}
