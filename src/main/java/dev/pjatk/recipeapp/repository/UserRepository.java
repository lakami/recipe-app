package dev.pjatk.recipeapp.repository;

import dev.pjatk.recipeapp.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")

    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Optional<User> findByEmail(String email);

    Optional<User> findByActivationToken(String activationToken);

    boolean existsByProfileUrl(String profileUrl);

    @EntityGraph(attributePaths = "favourites")
    Optional<User> findOneWithFavouriteRecipesByEmail(String email);
}