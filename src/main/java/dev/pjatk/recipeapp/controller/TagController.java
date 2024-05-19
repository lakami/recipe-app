package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.dto.response.TagDTO;
import dev.pjatk.recipeapp.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(TagController.TAG)
@RequiredArgsConstructor
public class TagController {
    protected static final String TAG = "/api/v1/tag";
    private final TagService tagService;

    @GetMapping
    public List<TagDTO> getAll() {
        return tagService.getAll();
    }

}
