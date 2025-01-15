package com.park.mall.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class MainController {

    @GetMapping("/main")
    public String mainPage() {
        return "admin/main";
    }
}
