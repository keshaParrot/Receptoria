package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class SuccessCase implements ResultCase{
    private final String message;

    public SuccessCase(String message) {
        this.message = message;
    }

    @Override
    public String getCaseMessage() {
        return message;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.OK;
    }
}
