package github.projectgroup.receptoria.model.dtos;

import github.projectgroup.receptoria.model.enities.RecipeIngredients;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import github.projectgroup.receptoria.model.enums.CookingMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeRequest {
    private List<RecipeIngredients> ingredients;
    private List<String> photoPaths;
    private String description;
    private String instructions;

    private MealCategory category;
    private int portionSize;
    private CookingMethod method;

    private Long ownerId;
    private Long coOwnerId;
}