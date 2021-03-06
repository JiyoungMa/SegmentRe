package segment.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import segment.Controller.Form.UserLoginForm;
import segment.Controller.Form.UserSignUpForm;
import segment.Entity.User;
import segment.Service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @GetMapping("/users/login")
    public String getLoginForm(Model model){
        model.addAttribute("userLoginForm", new UserLoginForm());

        return "users/createLoginForm";
    }

    @PostMapping("/users/login")
    public String userLogin(@Valid UserLoginForm form, BindingResult result, RedirectAttributes redirAttrs, Model model, HttpServletResponse response){
        if(result.hasErrors()){
            return "users/createLoginForm";
        }

        User user = new User();
        user.setUserRealId(form.getId());
        user.setUserPassword(form.getPassword());

        redirAttrs.addFlashAttribute("userId", user.getUserRealId());

        try {
            userService.login(user);
        }catch(Exception e){
            return "users/createLoginForm";
        }

        Cookie idCookie = new Cookie("userId", String.valueOf(user.getUserRealId()));
        idCookie.setPath("/");
        response.addCookie(idCookie);
        return "redirect:/";
    }
}
