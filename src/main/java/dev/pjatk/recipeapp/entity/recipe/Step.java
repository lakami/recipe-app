package dev.pjatk.recipeapp.entity.recipe;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "step")
@Getter
@Setter
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen")
    private Long id;

    @Size(min = 1, max = 1000)
    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private int number;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
