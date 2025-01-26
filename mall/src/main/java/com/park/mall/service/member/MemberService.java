package com.park.mall.service.member;

import com.park.mall.domain.member.Member;
import com.park.mall.repository.member.MemberJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberJpaRepository.save(member);
    }
}
