package github.projectgroup.receptoria.utils.result;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class Result<T> {

    @Getter
    private T value;
    private ResultCase resultCase;

    private Result(T value, ResultCase resultCase) {
        this.value = value;
        this.resultCase = resultCase;
    }

    public static <T> Result<T> success(T value, ResultCase resultCase){
        return new Result<T>(value, resultCase);
    }

    public static <T> Result<T> failure(ResultCase resultCase){
        return new Result<T>(null, resultCase);
    }

    public String getMessage(){
        return resultCase.getCaseMessage();
    }

    public HttpStatus getHttpStatus(){
        return resultCase.getHttpStatus();
    }

    public boolean isSuccess(){
        return resultCase.isSuccess();
    }

    public boolean isCaseInstanceOf(Class<? extends ResultCase> clazz){
        return clazz.isInstance(resultCase);
    }

    public boolean isValuePresent() {
        return value != null;
    }
}
