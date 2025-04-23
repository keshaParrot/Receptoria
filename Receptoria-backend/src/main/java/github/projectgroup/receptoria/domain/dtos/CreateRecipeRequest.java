package github.projectgroup.receptoria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import github.projectgroup.receptoria.domain.enums.CookingMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeRequest {
    private List<RecipeIngredientDTO> ingredients;
    private List<String> photoPaths;
    private String description;
    private int portionSize;
    private CookingMethod method;
    private Long ownerId;
    private Long coOwnerId;
}