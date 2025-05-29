package github.projectgroup.receptoria.model.dtos;

import github.projectgroup.receptoria.model.enities.RecipeIngredients;
import github.projectgroup.receptoria.model.enities.RecipePhoto;
import github.projectgroup.receptoria.model.enums.CookingMethod;
import github.projectgroup.receptoria.model.enums.MealCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecipeDTO {

    private Long id;
    private List<RecipeIngredients> ingredients;

    private String description;
    private String instructions;
    private int portionSize;

    private CookingMethod method;
    private MealCategory category;

    private UserPreviewDTO owner;
    private UserPreviewDTO coOwner;

    private List<RecipePhoto> photos;
    private List<ReactionDTO> reactions;
}
