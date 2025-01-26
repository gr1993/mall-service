package com.park.mall.web.mall.auth;

import com.park.mall.service.member.MemberService;
import com.park.mall.web.mall.auth.dto.MemberRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@Controller
public class MallAuthController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String loginView() {
        return "mall/auth/login";
    }

    @GetMapping("/register")
    public String registerView() {
        return "mall/auth/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(
            @Validated @RequestBody MemberRegister memberRegister,
            BindingResult bindingResult
    ) {
        if (!StringUtils.hasText(memberRegister.getConfirmPassword()) ||
                !memberRegister.getPassword().equals(memberRegister.getConfirmPassword())) {
            String errorMessage = messageSource.getMessage("NotEqual.member.password", null, Locale.getDefault());
            bindingResult.addError(new FieldError("memberRegister", "confirmPassword", null, false, null, null, errorMessage));
        }

        if (bindingResult.hasErrors()) {
            // 에러 메시지 리스트 반환
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error -> error.getField() + ":" + error.getDefaultMessage())
                            .toList()
            );
        }

        memberService.register(memberRegister.getMember());

        return ResponseEntity.ok().build();
    }
}
