package com.park.mall.service;

import com.park.mall.service.http.HttpClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class HttpClientServiceTest {

    @Autowired
    private HttpClientService httpClientService;

    @Test
    void getSuccess() {
        //given
        String url = "https://jsonplaceholder.typicode.com/users";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");

        //when
        String body = httpClientService.get(url, headers);

        //then
        Assertions.assertFalse(body.isEmpty());
    }

    @Test
    @Disabled
    void getFail() {
        //given
        String url = "https://jsonplaceholder.com/aaa";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");

        //when & then
        Assertions.assertThrows(RuntimeException.class, () -> {
            String body = httpClientService.get(url, headers);
        });
    }

    @Test
    void postSuccess() {
        //given
        String url = "https://jsonplaceholder.typicode.com/posts";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");
        String body = "{\"title\":\"park\"}";

        //when
        String response = httpClientService.post(url, headers, body);

        //then
        Assertions.assertFalse(response.isEmpty());
    }
}
