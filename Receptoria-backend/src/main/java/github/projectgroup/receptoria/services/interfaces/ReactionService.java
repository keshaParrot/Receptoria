package github.projectgroup.receptoria.services.interfaces;

import github.projectgroup.receptoria.domain.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.domain.dtos.ReactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReactionService {
    Page<ReactionDTO> getByRecipeId(Long recipeId, Pageable pageable);
    Page<ReactionDTO> getByUserId(Long userId, Pageable pageable);
    float getRecipeRating(Long recipeId);
    boolean create(CreateReactionRequest request);
    boolean update(Long id, float newRating);
    boolean deleteById(Long id);
}
