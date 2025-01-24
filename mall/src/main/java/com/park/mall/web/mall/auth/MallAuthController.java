package com.park.mall.web.mall.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MallAuthController {

    @GetMapping("/login")
    public String loginView() {
        return "mall/auth/login";
    }

    @GetMapping("/register")
    public String registerView() {
        return "mall/auth/register";
    }
}
