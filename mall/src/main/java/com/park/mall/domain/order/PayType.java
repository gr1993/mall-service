package com.park.mall.domain.order;

import java.util.Arrays;

public enum PayType {
    NORMAL("001");

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
}
