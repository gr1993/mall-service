package com.park.mall.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.park.mall.service.http.HttpClientService;
import com.park.mall.service.payment.dto.ReceiptInfo;
import com.park.mall.service.payment.dto.TokenRequestBody;
import com.park.mall.service.payment.dto.TokenResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BootpayService {

    @Autowired
    private HttpClientService httpClientService;

    @Value("${bootpay.url}")
    private String bootpayUrl;

    @Value("${bootpay.restapi.key}")
    private String apiKey;

    @Value("${bootpay.secret.key}")
    private String secretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAccessToken() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");

        TokenRequestBody tokenRequestBody = new TokenRequestBody();
        tokenRequestBody.setApplicationId(apiKey);
        tokenRequestBody.setPrivateKey(secretKey);

        try {
            String requestBody = objectMapper.writeValueAsString(tokenRequestBody);
            String responseBody = httpClientService.post(bootpayUrl + "/request/token", headers, requestBody);
            TokenResponseBody tokenResponseBody = objectMapper.readValue(responseBody, TokenResponseBody.class);
            return tokenResponseBody.getAccessToken();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ReceiptInfo getReceiptInfo(String receiptId) {
        String accessToken = getAccessToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");
        headers.put("Authorization", "Bearer " + accessToken);

        try {
            String responseBody = httpClientService.get(bootpayUrl + "/receipt/" + receiptId, headers);
            return objectMapper.readValue(responseBody, ReceiptInfo.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ReceiptInfo confirmReceipt(String receiptId) {
        String accessToken = getAccessToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");
        headers.put("Authorization", "Bearer " + accessToken);

        String requestBody = "{\"receipt_id\":\"" + receiptId + "\"}";

        try {
            String responseBody = httpClientService.post(bootpayUrl + "/confirm", headers, requestBody);
            return objectMapper.readValue(responseBody, ReceiptInfo.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ReceiptInfo cancel(String receiptId) {
        String accessToken = getAccessToken();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-type", "application/json; charset=UTF-8");
        headers.put("Authorization", "Bearer " + accessToken);

        String requestBody = "{\"receipt_id\":\"" + receiptId + "\"}";

        try {
            String responseBody = httpClientService.post(bootpayUrl + "/cancel", headers, requestBody);
            return objectMapper.readValue(responseBody, ReceiptInfo.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}