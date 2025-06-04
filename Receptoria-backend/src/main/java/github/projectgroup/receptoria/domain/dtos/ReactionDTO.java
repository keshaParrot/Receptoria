package github.projectgroup.receptoria.domain.dtos;

import github.projectgroup.receptoria.domain.enities.User;
import github.projectgroup.receptoria.domain.enities.UserRecipe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDTO {

    private Long id;
    private float reactionValue;
    private String content;
    private UserPreviewDTO user;
}
