package com.park.mall.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = "/";  // 기본 리다이렉트 URL

        // 사용자가 원래 접근하려던 URL로 리다이렉트하려면 세션에 저장된 URL을 사용
        if (request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST") != null) {
            SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            redirectUrl = savedRequest.getRedirectUrl();
        }

        // JSON으로 리다이렉트 URL을 응답
        response.setContentType("application/json");
        response.getWriter().write("{\"redirectUrl\":\"" + redirectUrl + "\"}");
    }
}
