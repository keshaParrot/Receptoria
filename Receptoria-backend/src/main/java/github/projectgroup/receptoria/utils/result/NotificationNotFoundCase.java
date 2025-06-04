package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundCase implements ResultCase{
    private final Long notificationId;

    public NotificationNotFoundCase(Long id) {
        notificationId = id;
    }

    @Override
    public String getCaseMessage() {
        return "Notification with id: "+ notificationId +" Not Found";
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
