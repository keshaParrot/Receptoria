package github.projectgroup.receptoria.domain.dtos;

import java.util.List;


import github.projectgroup.receptoria.domain.enities.RecipeIngredients;
import github.projectgroup.receptoria.domain.enities.RecipePhoto;
import github.projectgroup.receptoria.domain.enums.CookingMethod;
import github.projectgroup.receptoria.domain.enums.MealCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    /** Унікальний ідентифікатор */
    private Long id;

    /** Список інгредієнтів */
    private List<RecipeIngredients> ingredients;

    /** Опис рецепту */
    private String description;

    /** Інструкція з приготування */
    private String instruction;

    /** Розмір порції */
    private int portionSize;

    /** Метод приготування */
    private CookingMethod method;

    /** Категорія прийому їжі */
    private MealCategory category;

    /** Хто створив рецепт */
    private UserPreviewDTO owner;

    /** Співвласник (якщо є) */
    private UserPreviewDTO coOwner;
    private List<RecipePhoto> photos;
}