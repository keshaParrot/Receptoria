package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.domain.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.domain.dtos.ReactionDTO;
import github.projectgroup.receptoria.services.interfaces.ReactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    /**
     * Створити нову реакцію (рейтинг) до рецепту.
     * POST /api/reactions
     */
    @PostMapping
    public ResponseEntity<Void> reactRecipe(
            @RequestBody CreateReactionRequest request
    ) {
        boolean created = reactionService.create(request);
        if (created) {
            return ResponseEntity.status(201).build(); // 201 Created без тіла
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Оновити існуючу реакцію (змінити лише рейтинг).
     * PUT /api/reactions/{id}
     * Тіло запиту містить тільки новий рейтинг (float).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> reactRecipe(
            @PathVariable Long id,
            @RequestParam float newRating
    ) {
        boolean updated = reactionService.update(id, newRating);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Видалити реакцію за ID.
     * DELETE /api/reactions/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable("id") Long reactId) {
        boolean deleted = reactionService.deleteById(reactId);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
