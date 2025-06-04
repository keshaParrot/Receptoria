package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.domain.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.domain.dtos.ReactionDTO;
import github.projectgroup.receptoria.domain.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.domain.enities.Reaction;
import github.projectgroup.receptoria.domain.enities.User;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import github.projectgroup.receptoria.repositories.ReactionRepository;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.RecipeRepository;
import github.projectgroup.receptoria.services.interfaces.ReactionService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Override
    public Page<ReactionDTO> getByRecipeId(Long recipeId, Pageable pageable) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new IllegalArgumentException("Recipe not found: " + recipeId);
        }

        return reactionRepository
                .findAllByRatedRecipeId(recipeId, pageable)
                .map(this::toDto);
    }

    @Override
    public Page<ReactionDTO> getByUserId(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        return reactionRepository
                .findAllByOwnerId(userId, pageable)
                .map(this::toDto);
    }

    @Override
    public float getRecipeRating(Long recipeId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new IllegalArgumentException("Recipe not found: " + recipeId);
        }

        List<Reaction> reactions = reactionRepository.findAllByRatedRecipeId(recipeId, Pageable.unpaged()).getContent();
        if (reactions.isEmpty()) {
            return 0f;
        }
        float sum = 0f;
        for (Reaction r : reactions) {
            sum += r.getReactionValue();
        }
        return sum / reactions.size();
    }

    @Override
    public boolean create(CreateReactionRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found: " + request.getOwnerId()
                ));

        UserRecipe recipe = recipeRepository.findById(request.getRatedRecipeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipe not found: " + request.getRatedRecipeId()
                ));

        Reaction reaction = Reaction.builder()
                .reactionValue(request.getRating())
                .content(request.getContent())
                .owner(owner)
                .ratedRecipe(recipe)
                .build();

        reactionRepository.save(reaction);
        return true;
    }

    @Override
    public boolean update(Long id, float newRating) {
        Reaction existing = reactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Reaction not found: " + id
                ));

        existing.setReactionValue(newRating);
        reactionRepository.save(existing);
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        if (!reactionRepository.existsById(id)) {
            return false;
        }
        reactionRepository.deleteById(id);
        return true;
    }

    private ReactionDTO toDto(Reaction r) {
        UserPreviewDTO userDto = new UserPreviewDTO(
                r.getOwner().getId(),
                r.getOwner().getFirstName(),
                r.getOwner().getLastName()
        );

        ReactionDTO dto = new ReactionDTO();
        dto.setId(r.getId());
        dto.setReactionValue(r.getReactionValue());
        dto.setContent(r.getContent());
        dto.setUser(userDto);
        return dto;
    }
}
