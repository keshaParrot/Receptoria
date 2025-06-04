package github.projectgroup.receptoria.services.interfaces;


import github.projectgroup.receptoria.model.dtos.CreateRecipeRequest;
import github.projectgroup.receptoria.model.dtos.RecipeDTO;
import github.projectgroup.receptoria.model.enities.UserRecipe;
import github.projectgroup.receptoria.utils.result.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeService {
    Result<RecipeDTO> getById(Long id);

    Page<UserRecipe> getAllByUserId(Long userId, Pageable pageable);

    Page<RecipeDTO> getAllSavedByUserId(Long userId, Pageable pageable);

    Page<UserRecipe> findAllSorted(
            List<String> ingredients,
            String cookingMethod,
            Pageable pageable
    );

    Result<RecipeDTO> create(CreateRecipeRequest request);

    Result<RecipeDTO> update(Long id, CreateRecipeRequest request);

    boolean deleteById(Long id);

    Result<Void> setPublicStatus(Long id, boolean status);
}