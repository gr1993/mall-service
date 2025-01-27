package com.park.mall.service.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
@Slf4j
public class HttpClientService implements MyHttpClient {

    @Override
    public String get(String url, Map<String, String> headers) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET();

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::setHeader);
        }

        HttpRequest request = requestBuilder.uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            errorHandle(response);
            return response.body();
        } catch (InterruptedException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String post(String url, Map<String, String> headers, String body) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body));

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::setHeader);
        }

        HttpRequest request = requestBuilder.uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            errorHandle(response);
            return response.body();
        } catch (InterruptedException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void errorHandle(HttpResponse<String> response) {
        if (response.statusCode() >= 400) {
            log.error("bootpay error : " + response.body());
            throw new RuntimeException("http status code : " + response.statusCode());
        }
    }
}
