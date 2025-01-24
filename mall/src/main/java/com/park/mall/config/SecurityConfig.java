package com.park.mall.config;

import com.park.mall.security.AdminAuthenticationFailureHandler;
import com.park.mall.security.AdminUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .formLogin(form -> {
                form.loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login")
                    .defaultSuccessUrl("/admin/main", true)
                    .failureHandler(new AdminAuthenticationFailureHandler());
            })
            .logout(logout -> {
                logout.logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login")
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
                            "/",
                            "/product/**"
                    ).permitAll()
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

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        DaoAuthenticationProvider adminAuthProvider = new DaoAuthenticationProvider();
        adminAuthProvider.setUserDetailsService(adminUserDetailsService);
        adminAuthProvider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(adminAuthProvider);
        return auth.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
