package com.park.mall.controller.admin;

import com.park.mall.config.SecurityConfig;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.security.MemberUserDetailsService;
import com.park.mall.service.order.OrderService;
import com.park.mall.web.admin.order.OrderController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserDetailsService adminUserDetailsService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @MockitoBean
    private OrderService orderService;

    @Test
    @WithMockUser(roles = "admin")
    void orderView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/order"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "admin")
    void orderList() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                get("/admin/order/list")
                        .param("page", "1")
                        .param("limit", "10")
                        .param("colName", "id")
                        .param("dir", "asc")
        );

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "admin")
    void orderCountStat() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/order/count/stat"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "admin")
    void orderProductStat() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/order/product/stat"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
