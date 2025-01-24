package com.park.mall.controller.mall;

import com.park.mall.config.SecurityConfig;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.web.mall.product.MallProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MallProductController.class)
@Import(SecurityConfig.class)
public class MallProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserDetailsService adminUserDetailsService;

    @Test
    void productListView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/product/list"));

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
