package dev.pjatk.recipeapp.usecase.account;

import dev.pjatk.recipeapp.dto.response.AccountDTO;
import dev.pjatk.recipeapp.entity.Authority;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.util.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@RequiredArgsConstructor
public class GetAccountDataUseCase {
    private final UserRepository userRepository;

    @GetMapping
    public AccountDTO execute() {
        var user = SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneWithAuthoritiesByEmailIgnoreCase)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AccountDTO(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getImageUrl(),
                user.getProfileUrl(),
                user.getAuthorities().stream().map(Authority::getName).toList()
        );
    }
}
