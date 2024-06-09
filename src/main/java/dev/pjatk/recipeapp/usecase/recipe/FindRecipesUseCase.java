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

    public Page<RecipeDTO> execute(Pageable pageable,
                                   List<String> categories,
                                   List<String> dishes,
                                   List<String> tags,
                                   String search) {
        List<Specification<Recipe>> specifications = new LinkedList<>();
        if (categories != null && !categories.isEmpty()) {
            var spec = (Specification<Recipe>) (root, query, criteriaBuilder) -> root.join("categories").get("name").in(
                    categories);
            specifications.add(spec);
        }

        if (dishes != null && !dishes.isEmpty()) {
            var spec = (Specification<Recipe>) (root, query, criteriaBuilder) -> root.join("dishes").get("name").in(
                    dishes);
            specifications.add(spec);
        }

        if (tags != null && !tags.isEmpty()) {
            var spec = (Specification<Recipe>) (root, query, criteriaBuilder) -> root.join("tags").get("name").in(tags);
            specifications.add(spec);
        }

        if (search != null && !search.isBlank()) {
            search = search.trim();
            search = search.toLowerCase();
            String[] tokens = search.split("\\s+");

            List<Specification<Recipe>> textSpecifications = new LinkedList<>();
            Arrays.stream(tokens)
                    .map(token -> (Specification<Recipe>) (root, query, criteriaBuilder) -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")), "%" + token + "%"))
                    .forEach(textSpecifications::add);

            Arrays.stream(tokens)
                    .map(token -> (Specification<Recipe>) (root, query, criteriaBuilder) -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")), "%" + token + "%"))
                    .forEach(textSpecifications::add);

            Arrays.stream(tokens)
                    .map(token -> (Specification<Recipe>) (root, query, criteriaBuilder) -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("ingredients").get("name")), "%" + token + "%"))
                    .forEach(textSpecifications::add);

            Arrays.stream(tokens)
                    .map(token -> (Specification<Recipe>) (root, query, criteriaBuilder) -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.join("steps").get("description")), "%" + token + "%"))
                    .forEach(textSpecifications::add);

            textSpecifications.stream()
                    .reduce(Specification::or)
                    .ifPresent(specifications::add);
        }

        var spec = Specification.allOf(specifications);

        return recipeRepository
                .findAll(spec, pageable)
                .map(RecipeDTO::new);
    }


}
