package github.projectgroup.receptoria.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecipePreviewDTO {
    private Long id;
    private String title;
    private String previewPhotoUrl;
    private float averageRating;
}
