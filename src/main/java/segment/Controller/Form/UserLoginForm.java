package segment.Controller.Form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserLoginForm {

    @NotEmpty(message = "아이디는 필수입니다.")
    private String id;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;
}
