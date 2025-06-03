package github.projectgroup.receptoria.domain.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreviewDTO {
    private Long id;
    private String firstName;
    private String lastName;
}
