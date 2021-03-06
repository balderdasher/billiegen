package com.mrdios.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author CodePorter
 * @date 2017-10-27
 */
@Controller
public class FrontAction {

    @RequestMapping("/welcome")
    public String welcomeFront(Model model) {
        model.addAttribute("msg", "Welcome to front");
        return "index_front";
    }

    @RequestMapping("/welcome.json")
    @ResponseBody
    public String welcome() {
        return "Welcome to front";
    }
}
