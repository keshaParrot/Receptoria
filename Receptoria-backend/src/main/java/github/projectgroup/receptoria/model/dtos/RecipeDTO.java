package github.projectgroup.receptoria.model.dtos;


import github.projectgroup.receptoria.model.enities.RecipeIngredients;
import github.projectgroup.receptoria.model.enums.CookingMethod;
import github.projectgroup.receptoria.model.enums.MealCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



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