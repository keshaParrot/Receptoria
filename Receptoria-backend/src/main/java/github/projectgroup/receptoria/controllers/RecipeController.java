package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.UserRecipePreviewDTO;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<UserRecipe>> getAllByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                recipeService.getAllByUserId(userId, pageable)
        );
    }

    @GetMapping("/user/{userId}/saved")
    public ResponseEntity<Page<RecipeDTO>> getAllSavedByUserId(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                recipeService.getAllSavedByUserId(userId, pageable)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserRecipe>> search(
            @RequestParam List<String> ingredients,
            @RequestParam String cookingMethod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRecipe> result = recipeService.findAllSorted(
                ingredients, cookingMethod, pageable
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ) {
        return ResultMapper.toResponseEntity(recipeService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRecipePreviewDTO> update(
            @PathVariable Long id,
            @RequestBody UserRecipePreviewDTO dto
    ) {
        throw new RuntimeException("not implemented yet");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRecipe(
            @RequestPart("data") @Valid CreateRecipeRequest dto,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
    ) {
        dto.setPhotos(photos);

        return ResultMapper.toResponseEntity(recipeService.create(dto));
    }

    @PutMapping(path = "/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRecipe(
            @PathVariable Long id,
            @RequestBody CreateRecipeRequest request,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
    ) {
        return ResultMapper.toResponseEntity(recipeService.update(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable Long id
    ) {
        boolean deleted = recipeService.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/public")
    public ResponseEntity<Void> setPublicStatus(
            @PathVariable Long id,
            @RequestParam boolean status
    ) {
        recipeService.setPublicStatus(id, status);
        return ResponseEntity.noContent().build();
    }
}
