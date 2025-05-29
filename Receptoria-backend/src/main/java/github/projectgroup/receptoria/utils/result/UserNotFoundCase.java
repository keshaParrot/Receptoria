package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class UserNotFoundCase implements ResultCase {

    private Long userId;
    private String email;

    public UserNotFoundCase(Long userId) {
        this.userId = userId;
    }

    public UserNotFoundCase(String email) {
        this.email = email;
    }

    @Override
    public String getCaseMessage() {
        if (userId != null) return "User with id:"+ userId +"not found";
        else return "User with email:"+ email +" not found";

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
