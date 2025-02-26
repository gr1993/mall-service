package com.park.mall.service.order;

import com.park.mall.common.exception.PaymentException;
import com.park.mall.domain.member.Member;
import com.park.mall.domain.order.OrderDetails;
import com.park.mall.domain.order.Orders;
import com.park.mall.domain.order.PayType;
import com.park.mall.domain.order.Status;
import com.park.mall.domain.product.Product;
import com.park.mall.domain.product.ProductImg;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.repository.order.dto.OrderCountStatistics;
import com.park.mall.repository.order.dto.OrderSearchCondition;
import com.park.mall.repository.order.OrdersJpaRepository;
import com.park.mall.repository.order.OrdersQueryRepository;
import com.park.mall.repository.order.dto.ProductCountStatistics;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.service.order.dto.*;
import com.park.mall.service.payment.BootpayService;
import com.park.mall.service.payment.BootpayStatus;
import com.park.mall.service.payment.dto.ReceiptInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
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
    private OrdersQueryRepository ordersQueryRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    public Map<String, Object> searchMyOrders(Pageable pageable) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();
        Member member = memberJpaRepository.findById(a.getName()).orElseThrow();

        OrderSearchCondition condition = new OrderSearchCondition();
        condition.setMemberId(member.getId());

        return getResultData(condition, pageable);
    }

    public Map<String, Object> searchOrdersForAdmin(OrderSearchCondition condition, Pageable pageable) {
        return getResultData(condition, pageable);
    }

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

    public void cancelOrder(String orderId) {
        Orders orders = ordersJpaRepository.findById(orderId).orElseThrow();
        if (!Status.PAYMENT.equals(orders.getStatus())) {
            throw new RuntimeException("배송 준비중 이후 부터는 취소할 수 없습니다.");
        }

        bootpayService.cancel(orders.getReceiptId());
        orders.setStatus(Status.CANCEL);
    }

    public AdminOrderDetail getAdminOrderDetail(String orderId) {
        Orders orders = ordersJpaRepository.findById(orderId).orElseThrow();
        List<OrderDetails> orderDetailsList = orders.getOrderDetails();

        AdminOrderDetail adminOrderDetail = new AdminOrderDetail();
        adminOrderDetail.setId(orderId);
        adminOrderDetail.setMemberId(orders.getMember().getId());
        adminOrderDetail.setAddress(orders.getAddress());
        adminOrderDetail.setStatus(orders.getStatus().getCode());
        adminOrderDetail.setStatusText(orders.getStatus().getCodeText());
        adminOrderDetail.setPayType(orders.getPayType().getCode());
        adminOrderDetail.setPayTypeText(orders.getPayType().getCodeText());
        adminOrderDetail.setPayAmount(orders.getPayAmount());
        adminOrderDetail.setPayDate(orders.getPayDate());

        for (OrderDetails orderDetails : orderDetailsList) {
            OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
            orderDetailInfo.setProductId(orderDetails.getProduct().getId());
            orderDetailInfo.setProductName(orderDetails.getProductName());
            orderDetailInfo.setPrice(orderDetails.getPrice());
            orderDetailInfo.setQuantity(orderDetails.getQuantity());

            ProductImg productImg = orderDetails.getProduct().getProductImgs().get(0);
            orderDetailInfo.setMainImgPath(productImg.getMainImgPath());
            adminOrderDetail.getOrderDetailInfos().add(orderDetailInfo);
        }

        return adminOrderDetail;
    }

    /**
     * 최근 일주일 주문 수 통계 쿼리
     */
    public List<AdminOrderStat> getOrderCountForDay() {
        List<OrderCountStatistics> orderInfoList = ordersJpaRepository.getOrderCountForDay(
                LocalDateTime.now().minusDays(8),
                LocalDateTime.now(),
                Status.CONFIRM.getCode(),
                null
        );

        List<AdminOrderStat> adminOrderStatList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Optional<OrderCountStatistics> oOrderInfo = orderInfoList.stream()
                    .filter(o -> dateStr.equals(o.getOrderDate()))
                    .findAny();

            if (oOrderInfo.isEmpty()) {
                adminOrderStatList.add(new AdminOrderStat(dateStr, 0));
            } else {
                adminOrderStatList.add(new AdminOrderStat(dateStr, oOrderInfo.get().getOrderCount()));
            }
        }
        return adminOrderStatList;
    }

    public List<ProductCountStatistics> getOrderProductStat() {
        return ordersQueryRepository.getOrderProductStat();
    }


    private Map<String, Object> getResultData(OrderSearchCondition condition, Pageable pageable) {
        Page<Orders> page = ordersQueryRepository.searchPage(condition, pageable);
        List<Orders> content = page.getContent();
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for(Orders o : content) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setId(o.getId());
            orderInfo.setMemberId(o.getMember().getId());
            orderInfo.setStatus(o.getStatus().getCode());
            orderInfo.setStatusText(o.getStatus().getCodeText());
            orderInfo.setPayAmount(o.getPayAmount());
            orderInfo.setPayDate(o.getPayDate());

            for (OrderDetails orderDetails : o.getOrderDetails()) {
                Product product = orderDetails.getProduct();
                OrderDetailInfo orderDetailInfo = new OrderDetailInfo();
                orderDetailInfo.setProductId(product.getId());
                orderDetailInfo.setProductName(orderDetails.getProductName());
                orderDetailInfo.setQuantity(orderDetails.getQuantity());
                orderDetailInfo.setPrice(orderDetails.getPrice());
                orderDetailInfo.setMainImgPath(product.getProductImgs().get(0).getMainImgPath());

                orderInfo.getOrderDetailInfos().add(orderDetailInfo);
            }

            orderInfoList.add(orderInfo);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("page", page.getNumber() + 1);
        response.put("totalPages", page.getTotalPages());
        response.put("totalElements", page.getTotalElements());
        response.put("data", orderInfoList);
        return response;
    }

    private Orders validatePayment(OrderRequest orderRequest) {
        String receiptId = orderRequest.getReceiptId();
        ReceiptInfo receiptInfo = bootpayService.getReceiptInfo(receiptId);

        if (receiptInfo == null || !StringUtils.hasText(receiptInfo.getReceiptId())) {
            throw new PaymentException("유효하지 않은 결제 건");
        }

        // 검증하기도 전에 이미 승인되었다면 취소 처리
        if (BootpayStatus.COMPLETE.equals(receiptInfo.getStatus())) {
            bootpayService.cancel(receiptId);
            throw new PaymentException("검증 전 완료 건으로 취소");
        } else if (!BootpayStatus.CONFIRM_BEFORE.equals(receiptInfo.getStatus())) {
            throw new PaymentException("승인 가능한 상태가 아님 : " + receiptInfo.getStatus());
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
            orderDetails.setOrders(orders);
            orderDetails.setProduct(product);
            orderDetails.setProductName(product.getName());
            orderDetails.setPrice(product.getPrice());
            orderDetails.setQuantity(cartItem.getQuantity());
            orders.getOrderDetails().add(orderDetails);
        }

        // 결제 금액과 실제 상품 금액 합계가 다른 경우 취소
        if (totalPrice != paymentPrice) {
            throw new PaymentException("결제 금액이 올바르지 않습니다.");
        }

        //승인 처리
        receiptInfo = bootpayService.confirmReceipt(receiptId);
        if (!BootpayStatus.COMPLETE.equals(receiptInfo.getStatus())) {
            throw new PaymentException("정상적으로 승인되지 않았습니다.");
        }
        return orders;
    }
}
