package com.park.mall.service.product.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminProductDetail {

    private Long id;
    private String name;
    private Integer price;
    private String mainImg;
    private String mainPath;
    private String descImg;
    private String descPath;
}
