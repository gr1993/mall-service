package com.park.mall.web.mall.order;

import com.park.mall.service.order.OrderService;
import com.park.mall.service.order.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MallOrderController {

    @Autowired
    private OrderService orderService;

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

    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<?> orders(
            @Validated @RequestBody OrderRequest orderRequest,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            // 에러 메시지 리스트 반환
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error -> error.getField() + ":" + error.getDefaultMessage())
                            .toList()
            );
        }

        orderService.order(orderRequest);

        return ResponseEntity.ok().build();
    }
}
