package dev.pjatk.recipeapp.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthorityDTO(
        @NotNull @Size(max = 50) String name
) {}