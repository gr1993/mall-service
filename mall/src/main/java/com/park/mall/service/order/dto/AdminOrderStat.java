package com.park.mall.service.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminOrderStat {

    private String orderDate;
    private Integer orderCount;
}
