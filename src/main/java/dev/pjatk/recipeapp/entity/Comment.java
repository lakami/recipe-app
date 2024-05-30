package dev.pjatk.recipeapp.entity;

import dev.pjatk.recipeapp.entity.recipe.Recipe;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment extends AuditedEntityBase<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen")
    private Long id;

    @Size(max = 65535)
    @Column(nullable = false, length = 65535)
    private String content;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

}
