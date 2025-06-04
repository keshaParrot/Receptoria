package github.projectgroup.receptoria.model.dtos;

import github.projectgroup.receptoria.model.enities.RecipeIngredients;
import github.projectgroup.receptoria.model.enums.MealCategory;
import jakarta.validation.constraints.Size;
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
    private List<RecipeIngredients> ingredients;
    @Size(max = 5, message = "You can upload no more than 5 photos.")
    private List<MultipartFile> photos;
    private String description;
    private String instructions;

    private MealCategory category;
    private int portionSize;
    private CookingMethod method;

    private Long ownerId;
    private Long coOwnerId;
}