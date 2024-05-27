package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @Email @Size(min = 5, max = 128) @NotNull String email,
        @Size(min = 8, max = 64) @NotNull String password
) {}
