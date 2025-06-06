package github.projectgroup.receptoria.model.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReactionRequest {
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be at most 5.0")
    private float rating;
    @NotNull(message = "ownerId cannot be null")
    private Long ownerId;
    @NotNull(message = "content cannot be empty")
    @NotBlank(message = "content cannot be blank")
    private String content;
    @NotNull(message = "ratedRecipeId cannot be null")
    private Long ratedRecipeId;
}
