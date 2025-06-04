package github.projectgroup.receptoria.utils.result;

import github.projectgroup.receptoria.model.enities.VerificationCode;
import org.springframework.http.HttpStatus;

public class InvalidVerificationCode implements ResultCase {
    @Override
    public String getCaseMessage() {
        return "Verification code is expired or invalid";
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
