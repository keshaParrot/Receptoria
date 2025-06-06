package github.projectgroup.receptoria.model.mappers;

import github.projectgroup.receptoria.model.dtos.ServerErrorResponse;
import github.projectgroup.receptoria.utils.result.BadArgumentsCase;
import github.projectgroup.receptoria.utils.result.RecipeNotFoundCase;
import github.projectgroup.receptoria.utils.result.Result;
import github.projectgroup.receptoria.utils.result.UserNotFoundCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResultMapper {
    public static <T> ResponseEntity<?> toResponseEntity(Result<T> result) {
        if (result.isSuccess()) {
            if (result.isValuePresent())
                return ResponseEntity.ok(result.getValue());
            else
                return ResponseEntity.ok(result.getMessage());

        } else if (
                result.isCaseInstanceOf(UserNotFoundCase.class)
                        || result.isCaseInstanceOf(RecipeNotFoundCase.class)
        ) {
            return ResponseEntity.status(result.getHttpStatus()).body(result.getMessage());

        } else if (result.isCaseInstanceOf(BadArgumentsCase.class)) {
            if (result.getMessage()==null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ServerErrorResponse.builder()
                                .message(result.getMessage())
                                .build()
                );

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}