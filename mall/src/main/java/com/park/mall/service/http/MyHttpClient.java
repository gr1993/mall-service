package com.park.mall.service.http;

import java.util.Map;

public interface MyHttpClient {
    String get(String url, Map<String, String> headers);
    String post(String url, Map<String, String> headers, String body);
}
