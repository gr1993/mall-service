package com.park.mall.repository.member;

import com.park.mall.domain.member.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, String> {

    @Query("SELECT m FROM Member m ORDER BY m.createInfo.createdDtm DESC")
    List<Member> findTop10WithQuery(Pageable pageable);
}
