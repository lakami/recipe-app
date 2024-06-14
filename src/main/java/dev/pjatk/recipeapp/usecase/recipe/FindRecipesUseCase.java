package dev.pjatk.recipeapp.usecase.recipe;

import dev.pjatk.recipeapp.dto.response.RecipeDTO;
import dev.pjatk.recipeapp.entity.recipe.Recipe;
import dev.pjatk.recipeapp.repository.RecipeRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindRecipesUseCase {

    private final RecipeRepository recipeRepository;

    private static Specification<Recipe> byTags(List<String> tags) {
        return (root, query, criteriaBuilder) -> root
                .join("tags")
                .get("name")
                .in(tags);
    }

    private static Specification<Recipe> byDishes(List<String> dishes) {
        return (root, query, criteriaBuilder) -> root
                .join("dishes")
                .get("name")
                .in(dishes);
    }

    private static Specification<Recipe> byDiets(List<String> categories) {
        return (root, query, criteriaBuilder) -> root
                .join("categories")
                .get("name")
                .in(categories);
    }

    private static Specification<Recipe> byNames(List<String> tokens) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = tokens.stream()
                    .map(token -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + token + "%"))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }

    static Specification<Recipe> byDescriptions(List<String> tokens) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = tokens.stream()
                    .map(token -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            "%" + token + "%"))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }

    public Page<RecipeDTO> execute(Pageable pageable,
                                   List<String> categories,
                                   List<String> dishes,
                                   List<String> tags,
                                   String search) {
        Specification<Recipe> specification = Specification.where(null);
        if (categories != null && !categories.isEmpty()) {
            specification = specification.and(byDiets(categories));
        }

        if (dishes != null && !dishes.isEmpty()) {
            specification = specification.and(byDishes(dishes));
        }

        if (tags != null && !tags.isEmpty()) {
            specification = specification.and(byTags(tags));
        }

        if (search != null && !search.isBlank()) {
            var tokens = List.of(search.toLowerCase().split("\\s+"));
            var spec = byNames(tokens)
                    .or(byDescriptions(tokens));
            specification = specification.and(spec);
        }

        return recipeRepository
                .findAll(specification, pageable)
                .map(RecipeDTO::new);
    }
}
