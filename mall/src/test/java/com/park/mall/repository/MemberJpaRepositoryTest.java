package com.park.mall.repository;

import com.park.mall.domain.member.Member;
import com.park.mall.repository.member.MemberJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    void findTop10WithQuery() {
        //given
        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<Member> memberList = memberJpaRepository.findTop10WithQuery(pageable);

        //then
        Assertions.assertFalse(memberList.isEmpty());
    }
}
