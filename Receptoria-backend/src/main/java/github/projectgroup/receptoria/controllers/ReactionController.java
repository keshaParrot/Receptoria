package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.model.mappers.ResultMapper;
import github.projectgroup.receptoria.services.interfaces.ReactionService;

import github.projectgroup.receptoria.utils.result.Result;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<?> reactRecipe(
            @RequestBody CreateReactionRequest request
    ) {
        return ResultMapper.toResponseEntity(reactionService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> reactRecipe(
            @PathVariable Long id,
            @RequestParam float newRating
    ) {
        return ResultMapper.toResponseEntity(reactionService.update(id, newRating));
    }

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
