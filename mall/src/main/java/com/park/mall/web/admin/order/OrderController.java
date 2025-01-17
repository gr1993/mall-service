package com.park.mall.web.admin.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 관리자 > 주문 관리
 */
@Controller
@RequestMapping("/admin")
public class OrderController {

    @GetMapping("/order")
    public String orderView() {
        return "admin/order/order";
    }
}
