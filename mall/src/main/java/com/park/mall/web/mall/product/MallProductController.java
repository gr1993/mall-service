package com.park.mall.web.mall.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MallProductController {

    @GetMapping("/product/list")
    public String productView() {
        return "mall/product/list";
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "mall/product/detail";
    }
}
