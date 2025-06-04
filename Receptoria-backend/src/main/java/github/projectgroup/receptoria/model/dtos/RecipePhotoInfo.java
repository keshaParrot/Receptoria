package github.projectgroup.receptoria.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipePhotoInfo {
    private String url;
    private String fileName;
    private String contentType;
}
