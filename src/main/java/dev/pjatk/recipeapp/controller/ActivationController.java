package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.ActivationDTO;
import dev.pjatk.recipeapp.usecase.register.ActivateUserUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ActivationController.ACTIVATE)
@RequiredArgsConstructor
public class ActivationController {
    static final String ACTIVATE = "/api/v1/activate";
    private final ActivateUserUseCase activateUserUseCase;

    @PostMapping
    public void activate(@RequestParam(value="token") @Size(max = 20) String token,
                         @Valid @RequestBody ActivationDTO activationDTO) {
        activateUserUseCase.execute(token, activationDTO);
    }
}
