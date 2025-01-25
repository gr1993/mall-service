package com.park.mall.controller.mall;

import com.park.mall.config.SecurityConfig;
import com.park.mall.security.AdminUserDetailsService;
import com.park.mall.service.product.ProductService;
import com.park.mall.service.product.dto.AdminProductDetail;
import com.park.mall.service.product.dto.MallProductInfo;
import com.park.mall.web.mall.product.MallProductController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @MockitoBean
    private ProductService productService;

    @Test
    void productListView() throws Exception {
        //given
        String queryString = "?page=1&pageSize=9";
        List<MallProductInfo> productList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            MallProductInfo mallProductInfo = new MallProductInfo();
            mallProductInfo.setId((long) i);
            mallProductInfo.setName("park" + i);
            mallProductInfo.setPrice(i * 1000);
            mallProductInfo.setMainPath("/temp/park" + i);
            productList.add(mallProductInfo);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("page", 1);
        response.put("totalPages", 2);
        response.put("totalElements", 10);
        response.put("data", productList);

        Mockito.when(productService.searchProduct(Mockito.any(), Mockito.any(Pageable.class)))
                .thenReturn(response);

        //when
        ResultActions mvcAction = mockMvc.perform(get("/product/list" + queryString));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void productDetailView() throws Exception {
        //given
        AdminProductDetail productDetail = new AdminProductDetail();
        productDetail.setId(1L);
        productDetail.setName("park");
        productDetail.setPrice(1500);
        productDetail.setMainImg("mainFile.jpg");
        productDetail.setMainPath("/temp/mainFile.jpg");
        productDetail.setDescImg("descFile.jpg");
        productDetail.setDescPath("/temp/descFile.jpg");
        Mockito.when(productService.getProductDetail(Mockito.any()))
                .thenReturn(productDetail);

        //when
        ResultActions mvcAction = mockMvc.perform(get("/product/detail?productId=1"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    void productsInfo() throws Exception {
        //given
        Mockito.when(productService.getProductDetailList(Mockito.any()))
                .thenReturn(List.of());

        //when
        ResultActions mvcAction = mockMvc.perform(get("/products/info?productIdArr=1,2,3"));

        //then
        mvcAction
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
