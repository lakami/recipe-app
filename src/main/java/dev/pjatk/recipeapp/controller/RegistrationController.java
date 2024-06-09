package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.RegisterDTO;
import dev.pjatk.recipeapp.usecase.exception.TooWeakPasswordException;
import dev.pjatk.recipeapp.usecase.register.RegisterUseCase;
import dev.pjatk.recipeapp.util.Loggable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RegistrationController.REGISTER)
@RequiredArgsConstructor
public class RegistrationController implements Loggable {

    protected static final String REGISTER = "/api/v1/register";
    private final RegisterUseCase useCase;

    /**
     * Will register a new user. Sends activation email if e-mail is not already used.
     * <br/><br/>
     * {@code POST  /api/v1/register} : register a new user.
     *
     * @param registerDTO request body with email and password
     * @throws TooWeakPasswordException {@code 400 (Bad Request)} if password is too weak
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDTO registerDTO) {
        useCase.execute(registerDTO);
    }
}
