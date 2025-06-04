package github.projectgroup.receptoria.model.dtos;

import github.projectgroup.receptoria.model.enums.MealCategory;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    @Size(max = 3)
    private List<MealCategory> mainMealCategories;
}
