package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.entity.recipe.Recipe;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindRecipesUseCase {

    private final RecipeRepository recipeRepository;

    private static Specification<Recipe> tagsSpecification(List<String> tags) {
        return (root, query, criteriaBuilder) -> root
                .join("tags")
                .get("name")
                .in(tags);
    }

    private static Specification<Recipe> dishesSpecification(List<String> dishes) {
        return (root, query, criteriaBuilder) -> root
                .join("dishes")
                .get("name")
                .in(dishes);
    }

    private static Specification<Recipe> dietsSpecification(List<String> categories) {
        return (root, query, criteriaBuilder) -> root
                .join("categories")
                .get("name")
                .in(categories);
    }

    private static void setupTextSearchSpecification(String search,
                                                     List<Specification<Recipe>> specifications) {
        search = search.trim();
        search = search.toLowerCase();
        String[] tokens = search.split("\\s+");

        List<Specification<Recipe>> textSpecifications = new LinkedList<>();
        Arrays.stream(tokens)
                .map(FindRecipesUseCase::nameSpecification)
                .forEach(textSpecifications::add);

        Arrays.stream(tokens)
                .map(FindRecipesUseCase::descriptionSpecification)
                .forEach(textSpecifications::add);

        Arrays.stream(tokens)
                .map(FindRecipesUseCase::ingredientsSpecification)
                .forEach(textSpecifications::add);

        Arrays.stream(tokens)
                .map(FindRecipesUseCase::stepsSpecification)
                .forEach(textSpecifications::add);

        textSpecifications.stream()
                .reduce(Specification::or)
                .ifPresent(specifications::add);
    }

    private static Specification<Recipe> stepsSpecification(String token) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.join("steps").get("description")),
                "%" + token + "%");
    }

    private static Specification<Recipe> ingredientsSpecification(String token) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.join("ingredients").get("name")),
                "%" + token + "%");
    }

    private static Specification<Recipe> descriptionSpecification(String token) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("description")),
                "%" + token + "%");
    }

    private static Specification<Recipe> nameSpecification(String token) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + token + "%");
    }

    public Page<RecipeDTO> execute(Pageable pageable,
                                   List<String> categories,
                                   List<String> dishes,
                                   List<String> tags,
                                   String search) {
        List<Specification<Recipe>> specifications = new LinkedList<>();
        if (categories != null && !categories.isEmpty()) {
            var spec = dietsSpecification(categories);
            specifications.add(spec);
        }

        if (dishes != null && !dishes.isEmpty()) {
            var spec = dishesSpecification(dishes);
            specifications.add(spec);
        }

        if (tags != null && !tags.isEmpty()) {
            var spec = tagsSpecification(tags);
            specifications.add(spec);
        }

        if (search != null && !search.isBlank()) {
            setupTextSearchSpecification(search, specifications);
        }

        var spec = Specification.allOf(specifications);

        return recipeRepository
                .findAll(spec, pageable)
                .map(RecipeDTO::new);
    }
}
