package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public interface ResultCase {
    String getCaseMessage();
    boolean isSuccess();
    HttpStatus getHttpStatus();
}
