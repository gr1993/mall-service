package com.park.mall.security;

import com.park.mall.domain.admin.Admin;
import com.park.mall.repository.admin.AdminJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class AdminUserDetailsServiceTest {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;

    @Autowired
    private AdminJpaRepository adminJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void loadUserByUsername() {
        //given
        Admin admin = new Admin();
        admin.setId("park");
        String encodedPw = passwordEncoder.encode("1234");
        admin.setPassword(encodedPw);
        admin.setName("박강림");
        adminJpaRepository.save(admin);

        //when
        AdminUserDetails userDetails = (AdminUserDetails)adminUserDetailsService.loadUserByUsername(admin.getId());

        //then
        Assertions.assertEquals(admin.getId(), userDetails.getUsername());
        Assertions.assertEquals(admin.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(admin.getName(), userDetails.getName());
        GrantedAuthority grantedAuthority = userDetails.getAuthorities().iterator().next();
        Assertions.assertEquals("ROLE_admin", grantedAuthority.getAuthority());
    }
}
