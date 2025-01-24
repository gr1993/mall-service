package com.park.mall.web.mall.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MallOrderController {

    @GetMapping("/cart")
    public String cartView() {
        return "mall/order/cart";
    }

    @GetMapping("/orders/my")
    public String myOrderView() {
        return "mall/order/my";
    }

    @GetMapping("/payment")
    public String paymentView() {
        return "mall/order/payment";
    }
}
