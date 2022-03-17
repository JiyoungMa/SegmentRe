package segment.Exception;

import lombok.Getter;

@Getter
public class PasswordNotMatched extends RuntimeException{
    private ErrorCode errorCode;

    public PasswordNotMatched(String message,ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}