package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.recipe.Step;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Long> {
}