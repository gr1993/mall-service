package com.park.mall.web.admin.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 괸리자 > 상품 관리
 */
@Controller
@RequestMapping("/admin")
public class ProductController {

    @GetMapping("/product")
    public String productView() {
        return "admin/product/product";
    }
}
