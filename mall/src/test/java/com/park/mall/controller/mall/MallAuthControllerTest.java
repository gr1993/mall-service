package com.park.mall.controller.mall;

import com.park.mall.config.SecurityConfig;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.security.MemberUserDetailsService;
import com.park.mall.service.member.MemberService;
import com.park.mall.web.mall.auth.MallAuthController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(MallAuthController.class)
@Import(SecurityConfig.class)
public class MallAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserDetailsService adminUserDetailsService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @MockitoBean
    private MemberService memberService;

    @Test
    void loginView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/login"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void registerView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/register"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void registerNoBody() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                        .with(csrf())
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void registerInvalidValue() throws Exception {
        //given
        String jsonRequest = """
        {
            "name": "A",
            "email": "B",
            "id": "C",
            "password" : "D",
            "confirmPassword" : "E"
        }
        """;

        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf())
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void registerSuccess() throws Exception {
        //given
        String jsonRequest = """
        {
            "name": "박강림",
            "email": "test@naver.com",
            "id": "park123",
            "password" : "test123!!",
            "confirmPassword" : "test123!!"
        }
        """;

        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf())
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
