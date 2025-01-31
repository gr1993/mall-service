package com.park.mall.controller;

import com.park.mall.config.SecurityConfig;
import com.park.mall.domain.member.Member;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.security.MemberUserDetailsService;
import com.park.mall.web.admin.MainController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
@Import(SecurityConfig.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserDetailsService adminUserDetailsService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @MockitoBean
    private MemberJpaRepository memberJpaRepository;

    @Test
    @WithMockUser(roles = "admin")
    void mainPage() throws Exception {
        //given
        List<Member> memberList = new ArrayList<>();
        Member member = new Member();
        member.setId("park");
        member.setName("박강림");
        member.setEmail("test@naver.com");
        member.getCreateInfo().setCreatedDtm(LocalDateTime.now());
        memberList.add(member);

        Mockito.when(memberJpaRepository.findTop10WithQuery(Mockito.any()))
                .thenReturn(memberList);

        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/main"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
