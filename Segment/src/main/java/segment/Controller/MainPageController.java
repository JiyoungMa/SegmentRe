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

    @RequestMapping("/")
    public String mainPage(Model model){
        //log.info("mainpage controller");
        model.addAttribute("userId", model.getAttribute("userId"));
        return "mainpage";
    }
}