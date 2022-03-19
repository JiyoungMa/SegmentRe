package segment.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class MainPageController {

    @GetMapping("/")
    public String mainPage(Model model, RedirectAttributes redirectAttributes){
        //log.info("mainpage controller");
        Object userId = redirectAttributes.getAttribute("userId");
        model.addAttribute("userId", null);
        return "mainpage";
    }
}