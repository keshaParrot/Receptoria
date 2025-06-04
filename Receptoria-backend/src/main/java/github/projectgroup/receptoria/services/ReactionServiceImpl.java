package github.projectgroup.receptoria.services;


import github.projectgroup.receptoria.model.dtos.CreateReactionRequest;
import github.projectgroup.receptoria.model.dtos.ReactionDTO;
import github.projectgroup.receptoria.model.dtos.UserPreviewDTO;
import github.projectgroup.receptoria.model.enities.Reaction;
import github.projectgroup.receptoria.model.enities.User;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import github.projectgroup.receptoria.model.mappers.ReactionMapper;
import github.projectgroup.receptoria.repositories.ReactionRepository;
import github.projectgroup.receptoria.repositories.UserRepository;
import github.projectgroup.receptoria.repositories.RecipeRepository;
import github.projectgroup.receptoria.services.interfaces.ReactionService;

import github.projectgroup.receptoria.utils.result.ReactionNotFoundCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.SuccessCase;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ReactionMapper reactionMapper;

    @Override
    public Page<ReactionDTO> getByRecipeId(Long recipeId, Pageable pageable) {
        return reactionRepository
                .findAllByRatedRecipeId(recipeId, pageable)
                .map(this::toDto);
    }

    @Override
    public Page<ReactionDTO> getByUserId(Long userId, Pageable pageable) {
        return reactionRepository
                .findAllByOwnerId(userId, pageable)
                .map(this::toDto);
    }

    @Override
    public float getRecipeRating(Long recipeId) {

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
    public Result<ReactionDTO> create(CreateReactionRequest request) {
        Optional<User> owner = userRepository.findById(request.getOwnerId());
        if(owner.isEmpty()){
            return Result.failure(new UserNotFoundCase(request.getOwnerId()));
        }

        UserRecipe recipe = recipeRepository.findById(request.getRatedRecipeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recipe not found: " + request.getRatedRecipeId()
                ));

        Reaction reaction = Reaction.builder()
                .reactionValue(request.getRating())
                .content(request.getContent())
                .owner(owner.get())
                .ratedRecipe(recipe)
                .build();

        reactionRepository.save(reaction);
        return Result.success(toDto(reaction), new SuccessCase("good"));
    }

    @Override
    public Result<ReactionDTO> update(Long id, float newRating) {
        Optional<Reaction> existing = reactionRepository.findById(id);
        if(existing.isEmpty()){
            return Result.failure(new ReactionNotFoundCase(id));
        }
        existing.get().setReactionValue(newRating);
        reactionRepository.save(existing.get());
        return Result.success(toDto(existing.get()), new SuccessCase("good"));
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
        return reactionMapper.toDto(r);
    }
}
