package com.park.mall.repository.product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductSearchCondition {

    private Long id;
    private String name;
    private Integer minPrice;
    private Integer maxPrice;
}
