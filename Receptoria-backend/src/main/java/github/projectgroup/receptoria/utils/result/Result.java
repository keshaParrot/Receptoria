package github.projectgroup.receptoria.utils.result;

public class Result<T> {

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

    public T getValue() {
        return value;
    }

    public String getMessage(){
        return resultCase.getCaseMessage();
    }

    public boolean isSuccess(){
        return resultCase.isSuccess();
    }
}
