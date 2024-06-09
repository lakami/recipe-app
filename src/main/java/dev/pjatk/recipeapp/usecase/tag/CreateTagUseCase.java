package dev.pjatk.recipeapp.usecase.tag;

import dev.pjatk.recipeapp.dto.request.NewTagDTO;
import dev.pjatk.recipeapp.dto.response.TagDTO;
import dev.pjatk.recipeapp.entity.recipe.Tag;
import dev.pjatk.recipeapp.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreateTagUseCase {
    private final TagRepository tagRepository;

    public TagDTO execute(NewTagDTO newTagDTO) {
        var tag = new Tag();
        tag.setName(newTagDTO.name());
        tagRepository.save(tag);
        return new TagDTO(tag.getId(), tag.getName());
    }
}
