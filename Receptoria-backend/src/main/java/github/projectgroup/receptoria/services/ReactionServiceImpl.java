package github.projectgroup.receptoria.services;

import github.projectgroup.receptoria.domain.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.domain.dtos.ReactionDTO;
import github.projectgroup.receptoria.services.interfaces.ReactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ReactionServiceImpl implements ReactionService {
    @Override
    public Page<ReactionDTO> getByRecipeId(Long recipeId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReactionDTO> getByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public float getRecipeRating(Long recipeId) {
        return 0;
    }

    @Override
    public boolean create(CreateReactionRequest request) {
        return false;
    }

    @Override
    public boolean update(Long id, float newRating) {
        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
