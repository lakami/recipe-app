package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.ActivationDTO;
import dev.pjatk.recipeapp.entity.User;

import java.util.Optional;


public interface IUserService {
    User register(String email, String password) throws EmailAlreadyUsedException;
    Optional<User> activate(String activationToken, ActivationDTO missingData);
    String findNextProfileUrl(String firstName, String lastName);
    Optional<User> updateProfileUrl(String email, String profileUrl);

    Optional<User> getUserWithRecipes(String profileUrl);
}
