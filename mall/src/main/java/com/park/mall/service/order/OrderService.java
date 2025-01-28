package com.park.mall.service.order;

import com.park.mall.domain.member.Member;
import com.park.mall.domain.order.OrderDetails;
import com.park.mall.domain.order.Orders;
import com.park.mall.domain.order.PayType;
import com.park.mall.domain.order.Status;
import com.park.mall.domain.product.Product;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.repository.order.OrdersJpaRepository;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.service.order.dto.CartItem;
import com.park.mall.service.order.dto.OrderRequest;
import com.park.mall.service.payment.BootpayService;
import com.park.mall.service.payment.BootpayStatus;
import com.park.mall.service.payment.dto.ReceiptInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@Transactional
public class OrderService {

    @Autowired
    private BootpayService bootpayService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;


    public void order(OrderRequest orderRequest) {
        // 결제 검증 전 사용자 정보 불러오기
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();
        Member member = memberJpaRepository.findById(a.getName()).orElseThrow();

        // 결제 검증
        Orders orders = validatePayment(orderRequest);
        
        // 주문에 사용자 정보 저장
        orders.setMember(member);

        orders.setAddress(orderRequest.getAddress());
        orders.setStatus(Status.PAYMENT);
        orders.setPayDate(LocalDateTime.now());

        ordersJpaRepository.save(orders);
    }

    private Orders validatePayment(OrderRequest orderRequest) {
        String receiptId = orderRequest.getReceiptId();
        ReceiptInfo receiptInfo = bootpayService.getReceiptInfo(receiptId);

        if (receiptInfo == null || StringUtils.hasText(receiptInfo.getReceiptId())) {
            throw new RuntimeException("유효하지 않은 결제 건");
        }

        // 검증하기도 전에 이미 승인되었다면 취소 처리
        if (BootpayStatus.COMPLETE.equals(receiptInfo.getStatus())) {
            bootpayService.cancel(receiptId);
            throw new RuntimeException("검증 전 완료 건으로 취소");
        } else if (!BootpayStatus.CONFIRM_BEFORE.equals(receiptInfo.getStatus())) {
            throw new RuntimeException("승인 가능한 상태가 아님 : " + receiptInfo.getStatus());
        }

        // 결제 데이터 검증
        Orders orders = new Orders();
        orders.setReceiptId(receiptInfo.getReceiptId());
        orders.setId(receiptInfo.getOrderId());
        orders.setPayType(PayType.fromMethodSymbol(receiptInfo.getMethodSymbol()));
        orders.setPayAmount(receiptInfo.getPrice());

        int paymentPrice = orders.getPayAmount();
        int totalPrice = 0;
        for (CartItem cartItem : orderRequest.getCartItemList()) {
            Product product = productJpaRepository.findById(cartItem.getId()).orElseThrow();
            totalPrice += product.getPrice() * cartItem.getQuantity();

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrdersId(orders.getId());
            orderDetails.setProduct(product);
            orderDetails.setProductName(product.getName());
            orderDetails.setPrice(product.getPrice());
            orderDetails.setQuantity(cartItem.getQuantity());
            orders.getOrderDetails().add(orderDetails);
        }

        // 결제 금액과 실제 상품 금액 합계가 다른 경우 취소
        if (totalPrice != paymentPrice) {
            throw new RuntimeException("결제 금액이 올바르지 않습니다.");
        }

        //승인 처리
        receiptInfo = bootpayService.confirmReceipt(receiptId);
        if (!BootpayStatus.COMPLETE.equals(receiptInfo.getStatus())) {
            throw new RuntimeException("정상적으로 승인되지 않았습니다.");
        }
        return orders;
    }
}
