package com.park.mall.web.admin.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "admin/auth/login";
    }
}
