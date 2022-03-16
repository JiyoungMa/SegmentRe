package segment.Controller.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import segment.Service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/users/signup/checkDuplicationId")
    public CheckDuplicationIdResult checkDuplicationId(@RequestBody @Valid CheckDuplicationIdRequest request){
        boolean result = userService.checkUserIdDuplicate(request.getId());
        System.out.println("Request"+request.getId());

        return new CheckDuplicationIdResult(result);
    }

    @Data
    private static class CheckDuplicationIdRequest {

        @NotEmpty(message = "Id를 입력하세요")
        String id;
    }

    @Data
    private static class CheckDuplicationIdResult {
        boolean result;

        private CheckDuplicationIdResult(boolean result){
            this.result = result;
        }
    }

}
