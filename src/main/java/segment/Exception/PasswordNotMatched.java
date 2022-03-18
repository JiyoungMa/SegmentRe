package segment.Exception;

import lombok.Getter;

@Getter
public class PasswordNotMatched extends CustomException{
    private ErrorCode errorCode;

    public PasswordNotMatched(String message,ErrorCode errorCode){
        super(message,errorCode);
    }
}