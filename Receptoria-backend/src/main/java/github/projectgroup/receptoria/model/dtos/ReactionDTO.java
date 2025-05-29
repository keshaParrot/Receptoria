package github.projectgroup.receptoria.model.dtos;

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
