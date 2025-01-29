package com.park.mall.service.order.dto;

import com.park.mall.domain.order.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderInfo {

    private String id;

    private String memberId;

    private String status;

    private String statusText;

    private Integer payAmount;

    private LocalDateTime payDate;

    List<OrderDetailInfo> orderDetailInfos = new ArrayList<>();
}
