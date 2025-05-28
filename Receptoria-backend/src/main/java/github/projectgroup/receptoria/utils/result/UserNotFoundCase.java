package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class UserNotFoundCase implements ResultCase {

    private final Long userId;

    public UserNotFoundCase(Long userId) {
        this.userId = userId;
    }
    @Override
    public String getCaseMessage() {
        return "User with id:"+ userId +"not found";
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
