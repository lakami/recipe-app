package dev.pjatk.recipeapp.service;

import dev.pjatk.recipeapp.dto.request.NewTagDTO;
import dev.pjatk.recipeapp.dto.response.TagDTO;
import dev.pjatk.recipeapp.entity.recipe.Tag;
import dev.pjatk.recipeapp.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TagService {
    private final TagRepository tagRepository;

    public List<TagDTO> getAll() {
        return tagRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(tag -> new TagDTO(tag.getId(), tag.getName()))
                .toList();
    }

    public TagDTO create(NewTagDTO newTagDTO) {
        var tag = new Tag();
        tag.setName(newTagDTO.name());
        tagRepository.save(tag);
        return new TagDTO(tag.getId(), tag.getName());
    }
}
