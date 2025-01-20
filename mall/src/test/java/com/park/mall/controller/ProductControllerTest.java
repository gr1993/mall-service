package com.park.mall.controller;

import com.park.mall.service.product.ProductService;
import com.park.mall.web.admin.product.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void productView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/product"));

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        //System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void productList() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                get("/admin/product/list")
                        .param("page", "1")
                        .param("rows", "10")
                        .param("sidx", "id")
                        .param("sord", "desc")
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void productRegisterView() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/product/register"));

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void productRegisterNoBody() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(post("/admin/product/register"));

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void productRegisterInvalidValue() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/admin/product/register")
                .param("name", "A")
                .param("price", "0")
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().is4xxClientError())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void productRegisterSuccess() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/admin/product/register")
                        .param("mainImg", "mainImg.jpg")
                        .param("name", "park")
                        .param("price", "1000")
                        .param("descImg", "descImg.jpg")
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andReturn();
    }
}
