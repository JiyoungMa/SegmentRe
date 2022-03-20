package segment.Exception;

import lombok.Getter;

@Getter
public class ResourceNotExist extends CustomException{
    private ErrorCode errorCode;

    public ResourceNotExist(String message, ErrorCode errorCode){
        super(message,errorCode);
    }
}