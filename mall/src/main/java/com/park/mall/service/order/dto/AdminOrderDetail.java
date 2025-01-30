package com.park.mall.service.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminOrderDetail {

    private String id;

    private String memberId;

    private String address;

    private String status;

    private String statusText;

    private String payType;

    private String payTypeText;

    private Integer payAmount;

    private LocalDateTime payDate;

    List<OrderDetailInfo> orderDetailInfos = new ArrayList<>();
}
