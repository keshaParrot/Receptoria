package github.projectgroup.receptoria.utils.result;

import org.springframework.http.HttpStatus;

public class ReactionNotFoundCase implements ResultCase {
    private final Long reactionId;
    public ReactionNotFoundCase(Long id) {
        reactionId = id;
    }

    @Override
    public String getCaseMessage() {
        return "Reaction with id:"+ reactionId +"not found";
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
