package com.park.mall.web.mall.order;

import com.park.mall.service.order.OrderService;
import com.park.mall.service.order.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MallOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/cart")
    public String cartView() {
        return "mall/order/cart";
    }

    @GetMapping("/orders/my")
    public String myOrderView(
            @RequestParam(required = false, value = "page") Integer page,
            @RequestParam(required = false, value = "pageSize") Integer pageSize,
            Model model
    ) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 3;
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");
        Map<String, Object> response = orderService.searchMyOrders(pageable);

        model.addAttribute("orderList", response.get("data"));
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
