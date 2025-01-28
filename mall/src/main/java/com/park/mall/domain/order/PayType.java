package com.park.mall.domain.order;

import java.util.Arrays;

public enum PayType {
    PHONE("001"), // 휴대폰 소액결제
    CARD("002"), // 신용카드 결제
    BANK("003"), // 실시간 계좌이체
    VBANK("004"), // 가상계좌
    EASY("005"), // 간편
    KAKAOPAY("006"), // 카카오페이
    NAVERPAY("007"), // 네이버페이
    OTHER("999"); // 기타

    private final String code;

    PayType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PayType fromCode(String dbData) {
        return Arrays.stream(PayType.values())
                .filter(v -> v.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No PayType"));
    }

    public static PayType fromMethodSymbol(String methodSymbol) {
        return switch (methodSymbol) {
            case "phone" -> PayType.PHONE;
            case "card" -> PayType.CARD;
            case "bank" -> PayType.BANK;
            case "vbank" -> PayType.VBANK;
            case "easy" -> PayType.EASY;
            case "kakaopay" -> PayType.KAKAOPAY;
            case "naverpay" -> PayType.NAVERPAY;
            default -> PayType.OTHER;
        };
    }
}
