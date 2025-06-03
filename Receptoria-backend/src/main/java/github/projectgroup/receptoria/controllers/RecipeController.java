package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.domain.dtos.CreateRecipeRequest;
import github.projectgroup.receptoria.domain.dtos.RecipeDTO;
import github.projectgroup.receptoria.domain.dtos.UserRecipePreviewDTO;
import github.projectgroup.receptoria.domain.enities.RecipeIngredients;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import github.projectgroup.receptoria.services.interfaces.RecipeService;

import github.projectgroup.receptoria.utils.result.Result;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    /** Всі рецепти користувача (сутності) */
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

    /** Всі збережені рецепти користувача (DTO) */
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
            @RequestParam List<String> ingredients,      // ТІЛЬКИ НАЗВИ продуктів
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

    /** Окремий рецепт за id (DTO preview) */
    @GetMapping("/{id}")
    public ResponseEntity<UserRecipePreviewDTO> getById(
            @PathVariable Long id
    ) {
        // Тут треба виклик у recipeService.getById(id) → Result<RecipeDTO> → перевести в PreviewDTO
        throw new RuntimeException("not implemented yet");
    }

    /** Фільтрація за типами (приклад) */
    @GetMapping("/filter")
    public ResponseEntity<Page<UserRecipePreviewDTO>> filterByTypes(
            // додай сюди @RequestParam List<Followtype> types, Pageable pageable
    ) {
        throw new RuntimeException("not implemented yet");
    }

    /** Оновити прев’ю */
    @PutMapping("/{id}")
    public ResponseEntity<UserRecipePreviewDTO> update(
            @PathVariable Long id,
            @RequestBody UserRecipePreviewDTO dto
    ) {
        throw new RuntimeException("not implemented yet");
    }

    @PostMapping
    public ResponseEntity<Result<RecipeDTO>> createRecipe(
            @RequestBody CreateRecipeRequest request
    ) {
        Result<RecipeDTO> result = recipeService.create(request);
        return new ResponseEntity<>(result, result.getHttpStatus());
    }

    @PutMapping
    public ResponseEntity<Result<RecipeDTO>> updateRecipe(
            @RequestBody RecipeDTO request
    ) {
        Result<RecipeDTO> result = recipeService.update(request);
        return new ResponseEntity<>(result, result.getHttpStatus());
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
