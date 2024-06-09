package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.AccountDTO;
import dev.pjatk.recipeapp.usecase.account.GetAccountDataUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(AccountController.ACCOUNT)
public class AccountController {
    protected static final String ACCOUNT = "/api/v1/account";
    private final GetAccountDataUseCase getAccountDataUseCase;


    @GetMapping
    public AccountDTO getAccountData() {
        return getAccountDataUseCase.execute();
    }
}
