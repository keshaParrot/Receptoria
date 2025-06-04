package github.projectgroup.receptoria.domain.dtos;

import java.util.List;


import github.projectgroup.receptoria.domain.enities.RecipeIngredients;
import github.projectgroup.receptoria.domain.enums.CookingMethod;
import github.projectgroup.receptoria.domain.enums.MealCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private Long id;
    private List<RecipeIngredients> ingredients;
    private String description;
    private String instruction;
    private int portionSize;
    private CookingMethod method;
    private MealCategory category;
    private UserPreviewDTO owner;
    private UserPreviewDTO coOwner;
    private List<RecipePhotoInfo> photos;
}