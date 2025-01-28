package com.park.mall.service.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @NotBlank
    private String receiptId;

    @NotBlank
    private String address;

    private List<CartItem> cartItemList;
}
