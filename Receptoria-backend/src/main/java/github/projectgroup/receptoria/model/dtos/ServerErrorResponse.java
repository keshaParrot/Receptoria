package github.projectgroup.receptoria.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerErrorResponse {
    private String message;
}
