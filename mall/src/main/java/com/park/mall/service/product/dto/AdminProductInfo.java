package com.park.mall.service.product.dto;

import com.park.mall.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AdminProductInfo {

    private Long id;
    private String name;
    private Integer price;
    private LocalDateTime createdDtm;
    private String createId;

    public AdminProductInfo(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.price = p.getPrice();
        this.createdDtm = p.getCreateInfo().getCreatedDtm();
        this.createId = p.getCreateInfo().getCreateId();
    }
}
