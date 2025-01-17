package com.park.mall.web.admin.product;

import com.park.mall.service.product.ProductService;
import com.park.mall.service.product.dto.AdminProductSearch;
import com.park.mall.web.admin.common.JqGridInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 관리자 > 상품 관리
 */
@Controller
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public String productView() {
        return "admin/product/product";
    }

    @GetMapping("/product/list")
    @ResponseBody
    public Map<String, Object> productList(
            @ModelAttribute AdminProductSearch condition,
            @ModelAttribute JqGridInfo jqGridInfo) {

        Pageable pageable = PageRequest.of(
                jqGridInfo.getPage() - 1, // Pageable는 페이지 번호 0부터 시작
                jqGridInfo.getRows(),
                "asc".equals(jqGridInfo.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC,
                jqGridInfo.getSidx());

        return productService.searchProductForAdmin(condition, pageable);
    }

    @GetMapping("/product/register")
    public String productRegisterView() {
        return "admin/product/register";
    }
}
