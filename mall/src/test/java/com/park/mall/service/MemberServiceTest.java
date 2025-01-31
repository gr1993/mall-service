package com.park.mall.service;

import com.park.mall.domain.member.Member;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.service.member.MemberService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    void setup() throws Exception {
        member = new Member();
        member.setId("memberPark");
        member.setEmail("test@naver.com");
        member.setPassword("aaa345!!");
        member.setName("박강림");
    }

    @Test
    void register() {
        //when
        memberService.register(member);

        //then
        Member checkMember = memberJpaRepository.findById(member.getId()).orElseThrow();
        assertMember(member, checkMember);
    }

    @Test
    void update() {
        //given
        memberService.register(member);
        member.setName("memberPark");
        member.setEmail("test222@naver.com");

        //when
        memberJpaRepository.save(member);

        //then
        Member checkMember = memberJpaRepository.findById(member.getId()).orElseThrow();
        assertMember(member, checkMember);
    }

    @AfterEach
    void clear() throws Exception {
        memberJpaRepository.deleteById(member.getId());
    }

    private void assertMember(Member m1, Member m2) {
        Assertions.assertEquals(m1.getName(), m2.getName());
        Assertions.assertEquals(m1.getEmail(), m2.getEmail());
        Assertions.assertEquals(m1.getPassword(), m2.getPassword());
    }
}
