package dev.pjatk.recipeapp.usecase.user;

import dev.pjatk.recipeapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Component
@Transactional
public class FindNextProfileUrlUseCase {
    private static final SecureRandom SECURE_RANDOM;

    static {
        SECURE_RANDOM = new SecureRandom();
        SECURE_RANDOM.nextBytes(new byte[64]);
    }

    private final UserRepository userRepository;

    public String execute(String firstName, String lastName) {
        String profileUrl = firstName + "-" + lastName;
        if (userRepository.existsByProfileUrl(profileUrl)) {
            return profileUrl + "-" + RandomStringUtils.random(5, 0, 0, true, true, null, SECURE_RANDOM);
        }
        return profileUrl;
    }
}
