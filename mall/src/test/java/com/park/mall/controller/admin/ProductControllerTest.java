package com.park.mall.controller.admin;

import com.park.mall.config.SecurityConfig;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.security.MemberUserDetailsService;
import com.park.mall.service.product.ProductService;
import com.park.mall.service.product.dto.AdminProductDetail;
import com.park.mall.web.admin.product.ProductController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserDetailsService adminUserDetailsService;

    @MockitoBean
    private MemberUserDetailsService memberUserDetailsService;

    @MockitoBean
    private ProductService productService;

    @Test
    @WithMockUser(roles = "admin")
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
    @WithMockUser(roles = "admin")
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
    @WithMockUser(roles = "admin")
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
    @WithMockUser(roles = "admin")
    void productRegisterViewWithId() throws Exception {
        //given
        AdminProductDetail productDetail = new AdminProductDetail();
        productDetail.setId(1L);
        productDetail.setName("테스트 상품");
        productDetail.setPrice(1500);
        productDetail.setMainImg("mainImg.jpg");
        productDetail.setDescImg("descImg.jpg");
        productDetail.setMainPath("/temp/mainImg.jpg");
        productDetail.setDescPath("/temp/descImg.jpg");
        Mockito.when(productService.getProductDetail(Mockito.any(Long.class)))
                .thenReturn(productDetail);

        //when
        ResultActions mvcAction = mockMvc.perform(get("/admin/product/register?id=1"));

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(roles = "admin")
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
    @WithMockUser(roles = "admin")
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
    @WithMockUser(roles = "admin")
    void productRegisterSuccess() throws Exception {
        //given
        String jsonRequest = """
        {
            "mainImg": "mainImg.jpg",
            "name": "park",
            "price": "1000",
            "descImg": "descImg.jpg"
        }
        """;

        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/admin/product/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf())
        );

        //then
        MvcResult mvcResult = mvcAction
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "admin")
    void productDelete() throws Exception {
        //when
        ResultActions mvcAction = mockMvc.perform(
                post("/admin/product/delete/1")
                        .with(csrf())
        );

        //then
        mvcAction.andExpect(status().isOk());
    }
}
