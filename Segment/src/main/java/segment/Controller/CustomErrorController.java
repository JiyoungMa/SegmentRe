package segment.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import segment.Exception.CustomException;
import segment.Exception.ErrorCode;
import segment.Exception.PasswordNotMatched;
import segment.Exception.ResourceNotExist;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CustomErrorController implements ErrorController {

    @ExceptionHandler({ResourceNotExist.class, PasswordNotMatched.class})
    public ResponseEntity<Map<String,String>> exceptionHandler(CustomException e){
        ErrorCode errorCode = e.getErrorCode();

        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String,String> map = new HashMap<>();
        map.put("code", Integer.toString(errorCode.getStatus()));
        map.put("message", e.getMessage());
        map.put("timestamp", new Date().toString());

        return new ResponseEntity<>(map,responseHeaders,httpStatus);
    }


}