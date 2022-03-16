package segment.Controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import segment.Controller.Form.UserSignUpForm;
import segment.Entity.User;
import segment.Service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/signup")
    public String getSignUpForm(Model model){
        model.addAttribute("userSignUpForm", new UserSignUpForm());

        return "users/createSignUpForm";
    }

    @PostMapping("/users/signup")
    public String userSignUp(@Valid UserSignUpForm form, BindingResult result){
        if(result.hasErrors()){
            return "users/createSignUpForm";
        }

        User user = new User();
        user.setUserName(form.getName());
        user.setUserRealId(form.getId());
        user.setUserPassword(form.getPassword());

        userService.signUp(user);

        return "redirect:/"; //재로딩하면 안되는 부분은 리다이렉트로 보내버림
    }
}
