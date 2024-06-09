package dev.pjatk.recipeapp.usecase.comment;

import dev.pjatk.recipeapp.dto.response.CommentDTO;
import dev.pjatk.recipeapp.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GetRecipeCommentsUseCase {
    private final CommentRepository commentRepository;

    public List<CommentDTO> execute(Long recipeId) {
        return commentRepository.findAllByRecipeIdOrderByCreatedByDesc(recipeId)
                .stream()
                .map(CommentDTO::new)
                .toList();
    }
}
