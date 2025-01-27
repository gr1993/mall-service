package com.park.mall.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IdUtil {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateOrderId() {
        return "ORD" + getCurrentTimeFormatted();
    }

    private static String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(formatter);
    }
}
