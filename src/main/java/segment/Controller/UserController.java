package segment.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import segment.Controller.Form.UserSignUpForm;
import segment.Service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/signup")
    public String getSignUpForm(Model model){
        model.addAttribute("userSignUpForm", new UserSignUpForm());

        return "users/signup";
    }
}
