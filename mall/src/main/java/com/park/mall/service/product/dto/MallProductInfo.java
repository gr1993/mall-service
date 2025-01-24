package com.park.mall.service.product.dto;

import com.park.mall.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MallProductInfo {
    private Long id;
    private String name;
    private Integer price;
    private String mainPath;

    public MallProductInfo(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.price = p.getPrice();
        if (p.getProductImgs() != null && !p.getProductImgs().isEmpty()) {
            this.mainPath = p.getProductImgs().get(0).getMainImgPath();
        }
    }
}
