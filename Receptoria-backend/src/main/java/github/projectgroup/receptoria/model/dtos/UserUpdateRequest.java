package github.projectgroup.receptoria.model.dtos;

import github.projectgroup.receptoria.model.enums.MealCategory;
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
    private List<MealCategory> mainMealCategories; //MAX 3
}
