package com.park.mall.service.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailInfo {

    private Long productId;

    private String productName;

    private String mainImgPath;

    private Integer price;

    private Integer quantity;
}
