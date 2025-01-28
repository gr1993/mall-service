package com.park.mall.service;

import com.park.mall.common.exception.PaymentException;
import com.park.mall.domain.member.Member;
import com.park.mall.domain.order.Orders;
import com.park.mall.domain.order.Status;
import com.park.mall.domain.product.Product;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.repository.order.OrdersJpaRepository;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.security.MemberUserDetails;
import com.park.mall.service.order.OrderService;
import com.park.mall.service.order.dto.CartItem;
import com.park.mall.service.order.dto.OrderRequest;
import com.park.mall.service.payment.BootpayService;
import com.park.mall.service.payment.BootpayStatus;
import com.park.mall.service.payment.dto.ReceiptInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private BootpayService bootpayService;

    @MockitoBean
    private ProductJpaRepository productJpaRepository;

    @MockitoBean
    private OrdersJpaRepository ordersJpaRepository;

    @MockitoBean
    private MemberJpaRepository memberJpaRepository;

    List<CartItem> cartItemList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        Member member = new Member();
        member.setId("gr1993");
        member.setPassword(null);
        member.setName("박강림");
        member.setEmail("test@naver.com");
        MemberUserDetails memberUserDetails = new MemberUserDetails(member);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(memberUserDetails, null, memberUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 장바구니 셋팅
        CartItem cartItem1 = new CartItem();
        cartItem1.setId(1L);
        cartItem1.setQuantity(2);
        cartItemList.add(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setId(2L);
        cartItem2.setQuantity(3);
        cartItemList.add(cartItem2);
        
        // 상품 정보
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(1000);
        product1.setName("상품1");
        productList.add(product1);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(3000);
        product2.setName("상품2");
        productList.add(product2);
    }

    @Test
    void orderWithInvalidReceiptInfo() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiptId("a1b2c3d4");
        Mockito.when(memberJpaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Member()));

        //when & then
        PaymentException exception = Assertions.assertThrows(PaymentException.class, () -> {
            orderService.order(orderRequest);
        });

        log.info("오류 메시지 : {}", exception.getMessage());
    }

    @Test
    void orderWithCompleteReceipt() {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiptId("a1b2c3d4");
        Mockito.when(memberJpaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Member()));

        ReceiptInfo receiptInfo = new ReceiptInfo();
        receiptInfo.setReceiptId("a1b2c3d4");
        receiptInfo.setStatus(BootpayStatus.COMPLETE); // 검증이 된 결제 반환
        Mockito.when(bootpayService.getReceiptInfo(Mockito.any()))
                .thenReturn(receiptInfo);

        //when & then
        PaymentException exception = Assertions.assertThrows(PaymentException.class, () -> {
            orderService.order(orderRequest);
        });

        log.info("오류 메시지 : {}", exception.getMessage());
    }

    @Test
    void orderDifferentPrice () {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiptId("a1b2c3d4");
        Mockito.when(memberJpaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Member()));

        ReceiptInfo receiptInfo = new ReceiptInfo();
        receiptInfo.setReceiptId("a1b2c3d4");
        receiptInfo.setMethodSymbol("card");
        receiptInfo.setStatus(BootpayStatus.CONFIRM_BEFORE); // 검증 대기 상태
        receiptInfo.setPrice(10000); // 일부로 결제 금액 다르게 셋팅
        Mockito.when(bootpayService.getReceiptInfo(Mockito.any()))
                .thenReturn(receiptInfo);

        orderRequest.setCartItemList(cartItemList);

        Mockito.when(productJpaRepository.findById(1L))
                .thenReturn(Optional.of(productList.get(0)));
        Mockito.when(productJpaRepository.findById(2L))
                .thenReturn(Optional.of(productList.get(1)));

        //when & then
        PaymentException exception = Assertions.assertThrows(PaymentException.class, () -> {
            orderService.order(orderRequest);
        });

        log.info("오류 메시지 : {}", exception.getMessage());
    }

    @Test
    void orderFailConfirm () {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiptId("a1b2c3d4");
        Mockito.when(memberJpaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Member()));

        ReceiptInfo receiptInfo = new ReceiptInfo();
        receiptInfo.setReceiptId("a1b2c3d4");
        receiptInfo.setMethodSymbol("card");
        receiptInfo.setStatus(BootpayStatus.CONFIRM_BEFORE);
        receiptInfo.setPrice(11000);
        Mockito.when(bootpayService.getReceiptInfo(Mockito.any()))
                .thenReturn(receiptInfo);

        orderRequest.setCartItemList(cartItemList);

        Mockito.when(productJpaRepository.findById(1L))
                .thenReturn(Optional.of(productList.get(0)));
        Mockito.when(productJpaRepository.findById(2L))
                .thenReturn(Optional.of(productList.get(1)));

        receiptInfo = new ReceiptInfo();
        receiptInfo.setReceiptId("a1b2c3d4");
        receiptInfo.setStatus(BootpayStatus.CONFIRM_BEFORE);
        Mockito.when(bootpayService.confirmReceipt(Mockito.any()))
                .thenReturn(receiptInfo);

        //when & then
        PaymentException exception = Assertions.assertThrows(PaymentException.class, () -> {
            orderService.order(orderRequest);
        });

        log.info("오류 메시지 : {}", exception.getMessage());
    }

    @Test
    void orderSuccess () {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReceiptId("a1b2c3d4");
        Mockito.when(memberJpaRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(new Member()));

        ReceiptInfo receiptInfo = new ReceiptInfo();
        receiptInfo.setReceiptId("a1b2c3d4");
        receiptInfo.setMethodSymbol("card");
        receiptInfo.setStatus(BootpayStatus.CONFIRM_BEFORE);
        receiptInfo.setPrice(11000);
        Mockito.when(bootpayService.getReceiptInfo(Mockito.any()))
                .thenReturn(receiptInfo);

        orderRequest.setCartItemList(cartItemList);

        Mockito.when(productJpaRepository.findById(1L))
                .thenReturn(Optional.of(productList.get(0)));
        Mockito.when(productJpaRepository.findById(2L))
                .thenReturn(Optional.of(productList.get(1)));

        receiptInfo = new ReceiptInfo();
        receiptInfo.setReceiptId("a1b2c3d4");
        receiptInfo.setStatus(BootpayStatus.COMPLETE);
        Mockito.when(bootpayService.confirmReceipt(Mockito.any()))
                .thenReturn(receiptInfo);

        //when
        orderService.order(orderRequest);

        //then
        Mockito.verify(ordersJpaRepository)
                .save(Mockito.any(Orders.class));
    }
}
