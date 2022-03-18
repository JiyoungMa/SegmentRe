package segment.Exception;

import lombok.Getter;

@Getter
public class UserNotExist extends CustomException{
    private ErrorCode errorCode;

    public UserNotExist(String message,ErrorCode errorCode){
        super(message,errorCode);
    }
}