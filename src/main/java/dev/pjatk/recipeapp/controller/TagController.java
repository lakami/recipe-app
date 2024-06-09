package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.request.NewTagDTO;
import dev.pjatk.recipeapp.dto.response.TagDTO;
import dev.pjatk.recipeapp.usecase.tag.CreateTagUseCase;
import dev.pjatk.recipeapp.usecase.tag.GetAllTagsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(TagController.TAG)
@RequiredArgsConstructor
public class TagController {
    protected static final String TAG = "/api/v1/tag";
    private final GetAllTagsUseCase getAllTagsUseCase;
    private final CreateTagUseCase createTagUseCase;

    @GetMapping
    public List<TagDTO> getAll() {
        return getAllTagsUseCase.execute();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@Valid @RequestBody NewTagDTO newTagDTO) {
        return createTagUseCase.execute(newTagDTO);
    }

}
