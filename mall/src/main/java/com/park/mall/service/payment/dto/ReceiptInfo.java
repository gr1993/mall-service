package com.park.mall.service.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptInfo {

    @JsonProperty("receipt_id")
    private String receiptId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("tax_free")
    private Integer taxFree;

    @JsonProperty("cancelled_price")
    private Integer cancelledPrice;

    @JsonProperty("order_name")
    private String orderName;

    @JsonProperty("pg")
    private String pg;

    @JsonProperty("method")
    private String method;

    @JsonProperty("method_symbol")
    private String methodSymbol;

    @JsonProperty("method_origin")
    private String methodOrigin;

    @JsonProperty("method_origin_symbol")
    private String methodOriginSymbol;

    @JsonProperty("status")
    private Integer status;
}