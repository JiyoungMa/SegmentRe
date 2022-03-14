package segment.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class MainPageController {

    @RequestMapping("/")
    public String mainPage(){
        log.info("mainpage controller");
        return "mainpage";
    }
}