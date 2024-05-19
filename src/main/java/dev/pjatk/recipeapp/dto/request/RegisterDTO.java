package dev.pjatk.recipeapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @Email @Size(min = 5, max = 128) String email,
        @Size(min = 8, max = 64) String password
) {}
