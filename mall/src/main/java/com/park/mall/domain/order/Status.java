package com.park.mall.domain.order;

import java.util.Arrays;

public enum Status {
    PAYMENT("001"), // 결제완료
    PREPARE("002"), // 배송준비중
    SHIPPED("003"), // 배송중
    DELIVERED("004"), // 배송완료
    CONFIRM("005"), // 구매확정
    CANCEL("010"); //취소

    private final String code;

    Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Status fromCode(String dbData) {
        return Arrays.stream(Status.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No Status"));
    }

    public String getCodeText() {
        return switch (this.code) {
            case "001" -> "결제완료";
            case "002" -> "배송준비중";
            case "003" -> "배송중";
            case "004" -> "배송완료";
            case "005" -> "구매확정";
            case "010" -> "주문취소";
            default -> "";
        };
    }
}
