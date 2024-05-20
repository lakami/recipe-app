package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.usecase.GetUserFavouriteRecipesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(FavouritesController.FAVOURITES)
public class FavouritesController {
    protected static final String FAVOURITES = "/api/v1/favourites";

    private final GetUserFavouriteRecipesUseCase getUserFavouriteRecipesUseCase;

    @GetMapping
    public List<RecipeDTO> getUserFavourites() {
        return getUserFavouriteRecipesUseCase.getUserFavourites();
    }

    @DeleteMapping("/{id}")
    public void deleteFavourite(@PathVariable Long id) {
    }

    @PostMapping("/{id}")
    public void addFavourite(@PathVariable Long id) {
    }

}
