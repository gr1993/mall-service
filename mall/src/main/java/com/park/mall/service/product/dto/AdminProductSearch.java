package com.park.mall.service.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminProductSearch {
    private Long productId;
    private String productName;
    private Integer minPrice;
    private Integer maxPrice;
}
