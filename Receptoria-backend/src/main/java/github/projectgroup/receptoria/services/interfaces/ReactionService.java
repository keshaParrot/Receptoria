package github.projectgroup.receptoria.services.interfaces;


import github.projectgroup.receptoria.model.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.model.dtos.ReactionDTO;
import github.projectgroup.receptoria.utils.result.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReactionService {
    Page<ReactionDTO> getByRecipeId(Long recipeId, Pageable pageable);
    Page<ReactionDTO> getByUserId(Long userId, Pageable pageable);
    float getRecipeRating(Long recipeId);
    Result<ReactionDTO> create(CreateReactionRequest request);
    Result<ReactionDTO> update(Long id, float newRating);
    boolean deleteById(Long id);
}
