package com.park.mall.controller.mall;

import com.park.mall.common.IdUtil;
import com.park.mall.config.SecurityConfig;
import com.park.mall.domain.order.Status;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.security.MemberUserDetailsService;
import com.park.mall.service.order.OrderService;
import com.park.mall.service.order.dto.OrderDetailInfo;
import com.park.mall.service.order.dto.OrderInfo;
import com.park.mall.web.mall.order.MallOrderController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MallOrderController.class)
@Import(SecurityConfig.class)
public class MallOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserDetailsService adminUserDetailsService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @MockitoBean
    private OrderService orderService;

    @Test
    void cartView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/cart"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void myOrderView() throws Exception {
        //given
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setId(IdUtil.generateOrderId());
            orderInfo.setMemberId("park");
            orderInfo.setStatus(Status.PAYMENT.getCode());
            orderInfo.setStatusText(Status.PAYMENT.getCodeText());
            orderInfo.setPayAmount(1000);
            orderInfo.setPayDate(LocalDateTime.now());

            OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
            orderDetailInfo.setProductId(1L);
            orderDetailInfo.setProductName("상품1");
            orderDetailInfo.setPrice(1000);
            orderDetailInfo.setQuantity(1);
            orderDetailInfo.setMainImgPath("/mainImgPath");

            orderInfo.getOrderDetailInfos().add(orderDetailInfo);
            orderInfoList.add(orderInfo);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", orderInfoList);
        Mockito.when(orderService.searchMyOrders(Mockito.any()))
                .thenReturn(response);

        //when
        ResultActions mvcAction = mockMvc.perform(get("/orders/my"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void paymentView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/payment"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @WithMockUser
    void ordersNobody() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/api/orders")
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
    @WithMockUser
    void ordersSuccess() throws Exception {
        //given
        String jsonRequest = """
        {
            "receiptId": "a1w2e3r4",
            "ordersId": "ORD20250128123053",
            "address": "OO시 OO구 00동 000-000",
            "cartItemList": [
                {
                    "id": 1,
                    "quantity": 2
                },
                {
                    "id": 2,
                    "quantity": 4
                }
            ]
        }
        """;

        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/api/orders")
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
