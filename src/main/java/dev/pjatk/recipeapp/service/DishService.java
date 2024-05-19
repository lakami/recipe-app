package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.response.DishDTO;
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
}
