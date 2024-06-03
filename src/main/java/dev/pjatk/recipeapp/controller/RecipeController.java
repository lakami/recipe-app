package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewRecipeDTO;
import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.exception.ResourceNotFoundException;
import dev.pjatk.recipeapp.service.ForbiddenModificationException;
import dev.pjatk.recipeapp.service.RecipeService;
import dev.pjatk.recipeapp.usecase.AddRecipeUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(RecipeController.RECIPE)
public class RecipeController {
    protected static final String RECIPE = "/api/v1/recipe";

    private final RecipeService recipeService;
    private final AddRecipeUseCase addRecipeUseCase;

    @GetMapping("/promoted")
    public List<RecipeDTO> getPromotedRecipes() {
        return recipeService.getPromotedRecipes();
    }

    @PostMapping("/promote/{id}")
    public void promoteRecipe(@PathVariable Long id) {
        recipeService.promoteRecipe(id);
    }

    @DeleteMapping("/promote/{id}")
    public void demoteRecipe(@PathVariable Long id) {
        recipeService.demoteRecipe(id);
    }

    @GetMapping
    public Page<RecipeDTO> getAllRecipes(Pageable pageable,
                                         @RequestParam(required = false) List<String> diets,
                                         @RequestParam(required = false) List<String> dishes,
                                         @RequestParam(required = false) List<String> tags,
                                         @RequestParam(required = false) String search) {
        return recipeService.findAllMatchingRecipesForPage(pageable, diets, dishes, tags, search);
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    @RequestMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long createRecipe(@Valid @RequestPart("name") @Size(min = 1, max = 100) String name,
                             @Valid @RequestPart("name") @Size(min = 1, max = 1000) String description,
                             @Valid @RequestPart("name") @Size(min = 1, max = 10000) Integer preparationTime,
                             @Valid @RequestPart("name") @Size(min = 1, max = 100) Integer servings,
                             @Valid @RequestPart("name") @NotEmpty List<String> diets,
                             @Valid @RequestPart("name") @NotEmpty List<String> tags,
                             @Valid @RequestPart("name") @NotEmpty List<String> dishes,
                             @Valid @RequestPart("name") @NotEmpty List<String> steps,
                             @Valid @RequestPart("name") @NotEmpty List<String> ingredients,
                             @RequestParam("image") MultipartFile image) {
        var recipeDTO = new NewRecipeDTO(name,
                                         description,
                                         preparationTime,
                                         servings,
                                         diets,
                                         tags,
                                         dishes,
                                         steps,
                                         ingredients);
        return addRecipeUseCase.addRecipe(recipeDTO, image);
    }

    /**
     * @param id        recipe id
     * @param recipeDTO recipe data
     * @throws ResourceNotFoundException      if recipe with id not found
     * @throws ForbiddenModificationException if permission denied (recipe is not owned by user or user is not an admin)
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRecipe(@PathVariable Long id, @Valid @RequestBody NewRecipeDTO recipeDTO) {
        recipeService.updateRecipe(id, recipeDTO);
    }
}
