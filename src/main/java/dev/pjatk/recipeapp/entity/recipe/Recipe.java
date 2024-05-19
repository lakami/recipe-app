package dev.pjatk.recipeapp.entity.recipe;

import dev.pjatk.recipeapp.entity.AuditedEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "recipe")
@Getter
@Setter
public class Recipe extends AuditedEntityBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen")
    private Long id;

    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @Size(min = 1, max = 1000)
    @Column(nullable = false, length = 1000)
    private String description;

    @Size(min = 1, max = 10000)
    @Column(name = "preparation_time", nullable = false, length = 10000)
    private Integer preparationTime;

    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private Integer servings;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "recipe_tag",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "recipe_dish",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private Set<Dish> dishes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Step> steps;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Ingredient> ingredients;


}
