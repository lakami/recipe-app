package dev.pjatk.recipeapp.usecase.register;

import dev.pjatk.recipeapp.dto.request.RegisterDTO;
import dev.pjatk.recipeapp.entity.User;
import dev.pjatk.recipeapp.exception.EmailAlreadyUsedException;
import dev.pjatk.recipeapp.exception.TooWeakPasswordException;
import dev.pjatk.recipeapp.service.EmailService;
import dev.pjatk.recipeapp.usecase.user.CreateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegisterUseCaseTest {
    private CreateUserUseCase createUserUseCase;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        createUserUseCase = mock(CreateUserUseCase.class);
        emailService = mock(EmailService.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"password", "password123", "password123!@#"})
    void shouldThrowForWeakPassword(String password) {
        // given
        RegisterDTO dto = new RegisterDTO("email", password);
        RegisterUseCase registerUseCase = new RegisterUseCase(createUserUseCase, emailService);

        // when
        // then
        assertThrows(
                TooWeakPasswordException.class,
                () -> registerUseCase.execute(dto));
        verify(emailService, never()).senActivationEmail(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "098b0e81-62f3-432b-9885-178bf8ff43a7",
            "b0e8162f-3432-4b98-8517-8bf8ff43a709"
    })
    void shouldRegisterUserWithStrongPassword(String password) throws EmailAlreadyUsedException {
        // given
        var token = UUID.randomUUID().toString();
        String email = "email";
        RegisterDTO dto = new RegisterDTO(email, password);
        RegisterUseCase registerUseCase = new RegisterUseCase(createUserUseCase, emailService);
        when(createUserUseCase.execute(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    var user = new User();
                    user.setEmail(invocation.getArgument(0));
                    user.setActivationToken(token);
                    return user;
                });
        var userCaptor = ArgumentCaptor.forClass(User.class);

        // when
        registerUseCase.execute(dto);

        // then
        verify(createUserUseCase).execute(email, password);
        verify(emailService).senActivationEmail(userCaptor.capture());
        var user = userCaptor.getValue();
        assertEquals(email, user.getEmail());
        assertEquals(token, user.getActivationToken());
    }

    @Test
    void shouldNotThrowWhenEmailIsAlreadyUsedAsWeDoNotCommunicateThisToUser() throws EmailAlreadyUsedException {
        // given
        RegisterDTO dto = new RegisterDTO(
                "email",
                "098b0e81-62f3-432b-9885-178bf8ff43a7");
        RegisterUseCase registerUseCase = new RegisterUseCase(createUserUseCase, emailService);
        when(createUserUseCase.execute(anyString(), anyString()))
                .thenThrow(EmailAlreadyUsedException.class);

        // when
        registerUseCase.execute(dto);

        // then
        verify(createUserUseCase).execute(anyString(), anyString());
        verify(emailService, never()).senActivationEmail(any());
    }
}