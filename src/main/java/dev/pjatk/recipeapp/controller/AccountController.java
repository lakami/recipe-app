package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.AccountDTO;
import dev.pjatk.recipeapp.entity.Authority;
import dev.pjatk.recipeapp.repository.UserRepository;
import dev.pjatk.recipeapp.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(AccountController.ACCOUNT)
public class AccountController {
    protected static final String ACCOUNT = "/api/v1/account";
    private final UserRepository userRepository;

    @GetMapping
    public AccountDTO getAccountData() {
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
