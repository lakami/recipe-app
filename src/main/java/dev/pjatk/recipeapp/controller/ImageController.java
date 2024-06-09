package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.usecase.recipe.UpdateMainImageUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;

@RestController
@RequestMapping(ImageController.IMAGES)
public class ImageController {

    protected static final String IMAGES = "/api/v1/images";
    private final Path root;
    private final UpdateMainImageUseCase updateMainImageUseCase;

    public ImageController(@Value("${recipe.app-dir}") String root, UpdateMainImageUseCase updateMainImageUseCase) {
        this.root = Path.of(root);
        this.updateMainImageUseCase = updateMainImageUseCase;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public void updateMainImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        updateMainImageUseCase.execute(id, image);
    }

//    // TODO: Implement this endpoint when multi-image support will be added
//    @PostMapping("/{id}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void addImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
//    }

}
