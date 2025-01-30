package com.park.mall.web.admin.order;

import com.park.mall.repository.order.OrderSearchCondition;
import com.park.mall.service.order.OrderService;
import com.park.mall.web.admin.common.GridJsInfo;
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
 * 관리자 > 주문 관리
 */
@Controller
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/order")
    public String orderView() {
        return "admin/order/order";
    }

    @GetMapping("/order/list")
    @ResponseBody
    public Map<String, Object> orderList(
            @ModelAttribute OrderSearchCondition condition,
            @ModelAttribute GridJsInfo gridJsInfo) {

        Pageable pageable = PageRequest.of(
                gridJsInfo.getPage() - 1,
                gridJsInfo.getLimit(),
                "asc".equals(gridJsInfo.getDir()) ? Sort.Direction.ASC : Sort.Direction.DESC,
                gridJsInfo.getColName());

        return orderService.searchOrdersForAdmin(condition, pageable);
    }
}
