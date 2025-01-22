package com.park.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .formLogin(form -> {
                form.loginPage("/admin/login");
            })
            .logout(logout -> {
                logout.logoutUrl("/logout")
                      .deleteCookies("JSESSIONID", "remember-me");
            })
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests
                    .requestMatchers(
                            "/admin/login",
                            "/admin/logout",
                            "/admin/file/**"
                    ).permitAll()
                    .requestMatchers("/admin/**").hasAnyRole("admin")
                    .requestMatchers(
                            "/css/**",
                            "/js/**",
                            "/img/**",
                            "/error"
                    ).permitAll()
                    .anyRequest().authenticated();
            });

        return http.build();
    }
}
