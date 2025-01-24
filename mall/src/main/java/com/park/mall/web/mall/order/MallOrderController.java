package com.park.mall.web.mall.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MallOrderController {

    @GetMapping("/cart")
    public String productView() {
        return "mall/order/cart";
    }
}
