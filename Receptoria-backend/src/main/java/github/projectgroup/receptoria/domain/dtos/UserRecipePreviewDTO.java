package github.projectgroup.receptoria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecipePreviewDTO {
    private Long id;
    private String title;
    private RecipePhotoInfo photo;
    private float averageRating;
}
