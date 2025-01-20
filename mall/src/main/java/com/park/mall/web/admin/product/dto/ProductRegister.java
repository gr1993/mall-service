package com.park.mall.web.admin.product.dto;

import com.park.mall.domain.product.Product;
import com.park.mall.domain.product.ProductImg;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ProductRegister {

    @NotBlank(message = "{NotBlank}")
    private String mainImg;

    @NotBlank(message = "{NotBlank.product.name}")
    @Size(min = 2, max = 100, message = "{Size.product.name}")
    private String name;

    @NotNull(message = "{NotNull.product.price}")
    @Range(min = 1000, message = "{Range.product.price}")
    private Integer price;

    private String descImg;

    public Product convertToProduct() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);

        ProductImg productImg = new ProductImg();
        productImg.setMainImgName(this.mainImg);
        productImg.setDescImgName(this.descImg);
        product.getProductImgs().add(productImg);
        return product;
    }
}
