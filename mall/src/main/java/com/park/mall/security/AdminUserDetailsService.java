package com.park.mall.security;

import com.park.mall.domain.admin.Admin;
import com.park.mall.repository.admin.AdminJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminJpaRepository adminJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminJpaRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("관리자 아이디가 존재하지 않습니다."));

        return new AdminUserDetails(admin);
    }
}
