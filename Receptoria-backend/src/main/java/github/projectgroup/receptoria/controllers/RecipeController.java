package github.projectgroup.receptoria.controllers;

import github.projectgroup.receptoria.domain.dtos.UserRecipePreviewDTO;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import github.projectgroup.receptoria.domain.enums.Followtype;
import org.springframework.data.domain.Page;

public class RecipeController {

    public Page<UserRecipe> getAllByUserId(Long userId){
        throw new RuntimeException("not implemented yet");
    }

    public Page<UserRecipePreviewDTO> getById(Long id){
        throw new RuntimeException("not implemented yet");
    }

    public Page<UserRecipePreviewDTO> filterByTypes(){
        throw new RuntimeException("not implemented yet");
    }

    public UserRecipePreviewDTO update(Long id, UserRecipePreviewDTO dto){
        throw new RuntimeException("not implemented yet");
    }

}
