package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.model.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.model.mappers.ResultMapper;
import github.projectgroup.receptoria.services.interfaces.ReactionService;

import github.projectgroup.receptoria.utils.result.Result;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@reactionSecurity.canEdit(authentication, #id)")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(
            @PathVariable Long id,
            @RequestParam float newRating
    ) {
        return ResultMapper.toResponseEntity(reactionService.update(id, newRating));
    }

    @PreAuthorize("@reactionSecurity.canEdit(authentication, #id)")
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
