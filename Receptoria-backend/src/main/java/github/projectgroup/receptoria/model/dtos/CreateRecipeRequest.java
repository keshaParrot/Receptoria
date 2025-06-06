package github.projectgroup.receptoria.model.dtos;

import github.projectgroup.receptoria.model.enities.RecipeIngredients;
import github.projectgroup.receptoria.model.enums.MealCategory;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import github.projectgroup.receptoria.model.enums.CookingMethod;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeRequest {
    @NotNull(message = "Ingredients list cannot be null")
    @NotEmpty(message = "At least one ingredient is required")
    private List<RecipeIngredients> ingredients;
    @Size(max = 5, message = "You can upload no more than 5 photos.")
    private List<MultipartFile> photos;
    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Instructions cannot be null")
    @NotBlank(message = "Instructions cannot be blank")
    private String instructions;

    @NotNull(message = "Category cannot be null")
    private MealCategory category;
    @Min(value = 1, message = "Portion size must be at least 1")
    private int portionSize;
    @NotNull(message = "Cooking method cannot be null")
    private CookingMethod method;

    @NotNull(message = "Owner ID cannot be null")
    private Long ownerId;
    private Long coOwnerId;
}