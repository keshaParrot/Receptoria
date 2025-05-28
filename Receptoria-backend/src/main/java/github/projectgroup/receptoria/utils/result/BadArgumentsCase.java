package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class BadArgumentsCase implements ResultCase{

    private final String message;

    public BadArgumentsCase(String message) {
        this.message = message;
    }

    @Override
    public String getCaseMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}
