package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActivationDTO(
        @NotNull @Size(max = 50) @NotBlank String firstName,
        @NotNull @Size(max = 50) @NotBlank String lastName
) {}
