package github.projectgroup.receptoria.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReactionRequest {
    private float rating;
    private Long ownerId;
    private Long ratedRecipeId;
}
