package com.park.mall.repository.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductCountStatistics {
    private String productName;
    private Long orderCount;
}
