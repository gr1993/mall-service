package com.park.mall.service;

import com.park.mall.service.http.HttpClientService;
import com.park.mall.service.payment.BootpayService;
import com.park.mall.service.payment.dto.ReceiptInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class BootpayServiceTest {

    @Autowired
    private BootpayService bootpayService;

    @MockitoBean
    private HttpClientService httpClientService;

    @BeforeEach
    void setup() {
        String json = """
        {
            "expire_in": 1800,
            "access_token": "c553086e8e256dec37624b79119fb4158feedb36566ea5f5f43432f7292aebcf"
        }
        """;
        Mockito.when(httpClientService.post(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(json);
    }

    @Test
    void getAccessToken() {
        //when
        String token = bootpayService.getAccessToken();

        //then
        Assertions.assertEquals("c553086e8e256dec37624b79119fb4158feedb36566ea5f5f43432f7292aebcf", token);
    }

    @Test
    void getReceipt() {
        //given
        String json = """
        {
          "receipt_id": "6244f60c1fc19202e42e8c4e",
          "order_id": "1648686604470",
          "price": 1000,
          "tax_free": 0,
          "cancelled_price": 0,
          "cancelled_tax_free": 0,
          "order_name": "결제 테스트 스웻터",
          "company_name": "주) 부트페이",
          "gateway_url": "https://gw.bootpay.co.kr",
          "metadata": {
            "test": 1
          },
          "sandbox": true,
          "pg": "KCP",
          "method": "카드",
          "method_symbol": "card",
          "method_origin": "카드",
          "method_origin_symbol": "card",
          "currency": "KRW",
          "receipt_url": "https://door.bootpay.co.kr/receipt/WWlMbmFWRmlxcGUwb3Y3T1Q5b0R5clBjc2ZyUVhHVGpwamZBNS9KYXNhbmdB%0AZz09LS1zM1ZueWpsY0hCT3VPSmxsLS1EcVVGcitsVFBoQ3hCR1UyNFNhK0tB%0APT0%3D%0A",
          "purchased_at": "2022-03-31T09:30:29+09:00",
          "cancelled_at": null,
          "requested_at": "2022-03-31T09:30:04+09:00",
          "escrow_status_locale": null,
          "escrow_status": null,
          "status_locale": "결제완료",
          "status": 1,
          "card_data": {
            "card_approve_no": "16423357",
            "card_no": "5428790000000009",
            "card_quota": "00",
            "card_company_code": "CCLG",
            "card_company": "신한카드",
            "receipt_url": "https:\\\\/\\\\/npg.nicepay.co.kr\\\\/issue\\\\/IssueLoader.do?type=0&InnerWin=Y&TID=nicnaver0m01012207111634232099"
          }
        }
        """;
        Mockito.when(httpClientService.get(Mockito.any(), Mockito.any()))
                .thenReturn(json);

        //when
        ReceiptInfo receiptInfo = bootpayService.getReceiptInfo("6244f60c1fc19202e42e8c4e");
        Assertions.assertEquals("6244f60c1fc19202e42e8c4e", receiptInfo.getReceiptId());
        Assertions.assertEquals("1648686604470", receiptInfo.getOrderId());
        Assertions.assertEquals(1000, receiptInfo.getPrice());
        Assertions.assertEquals(0, receiptInfo.getTaxFree());
        Assertions.assertEquals(0, receiptInfo.getCancelledPrice());
        Assertions.assertEquals("결제 테스트 스웻터", receiptInfo.getOrderName());
        Assertions.assertEquals("KCP", receiptInfo.getPg());
        Assertions.assertEquals("카드", receiptInfo.getMethod());
        Assertions.assertEquals("카드", receiptInfo.getMethodOrigin());
        Assertions.assertEquals("card", receiptInfo.getMethodOriginSymbol());
        Assertions.assertEquals(1, receiptInfo.getStatus());
    }

    @Test
    void confirmReceiptFail() {
        //given
        Mockito.when(httpClientService.post(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("http error code : 400")); // 상태가 결제 대기 상태가 아니면 400 응답 반환

        //when & then
        Assertions.assertThrows(RuntimeException.class, () -> {
            bootpayService.confirmReceipt("67973f2753d4835c7c9a16fe");
        });
    }

    @Test
    void confirmReceiptSuccess() {
        //given
        String json = """
        {
          "receipt_id": "6244f60c1fc19202e42e8c4e",
          "status": 1
        }
        """;
        Mockito.when(httpClientService.post(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(json);

        //when
        ReceiptInfo receiptInfo = bootpayService.confirmReceipt("6244f60c1fc19202e42e8c4e");

        //then
        Assertions.assertEquals("6244f60c1fc19202e42e8c4e", receiptInfo.getReceiptId());
        Assertions.assertEquals(1, receiptInfo.getStatus());
    }

    @Test
    void cancel() {
        //given
        String json = """
        {
          "receipt_id": "6244f60c1fc19202e42e8c4e",
          "status": 20
        }
        """;
        Mockito.when(httpClientService.post(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(json);

        //when
        ReceiptInfo receiptInfo = bootpayService.cancel("6244f60c1fc19202e42e8c4e");

        //then
        Assertions.assertEquals("6244f60c1fc19202e42e8c4e", receiptInfo.getReceiptId());
        Assertions.assertEquals(20, receiptInfo.getStatus());
    }
}
