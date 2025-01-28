package com.park.mall.repository;

import com.park.mall.common.IdUtil;
import com.park.mall.domain.common.CodeYn;
import com.park.mall.domain.member.Member;
import com.park.mall.domain.order.OrderDetails;
import com.park.mall.domain.order.Orders;
import com.park.mall.domain.order.PayType;
import com.park.mall.domain.order.Status;
import com.park.mall.domain.product.Product;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.repository.order.OrdersJpaRepository;
import com.park.mall.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrdersJpaRepositoryTest {

    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private Member member;

    private final List<Product> productList = new ArrayList<>();

    private Orders orders;

    @BeforeAll
    public void setupAll() {
        member = new Member();
        member.setId("ordersRepoTest");
        member.setName("테스터");
        member.setEmail("test@naver.com");
        memberJpaRepository.save(member);

        for (int i = 1; i <= 2; i++) {
            Product product = new Product();
            product.setName("상품" + i);
            product.setPrice(1000 * i);
            productJpaRepository.save(product);
            productList.add(product);
        }
    }

    @BeforeEach
    public void setup() {
        orders = new Orders();
        orders.setId(IdUtil.generateOrderId());
        orders.setMember(member);
        orders.setAddress("서울시 구로구 디지털로 00길 00");
        orders.setStatus(Status.PREPARE);
        orders.setPayType(PayType.CARD);
        orders.setPayAmount(10000);
        orders.setPayDate(LocalDateTime.now());
        orders.setCancelYn(CodeYn.N);
        orders.setReceiptId("abcd123");

        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setOrdersId(orders.getId());
        orderDetails1.setProduct(productList.get(0));
        orderDetails1.setProductName(productList.get(0).getName());
        orderDetails1.setPrice(productList.get(0).getPrice());
        orderDetails1.setQuantity(1);

        OrderDetails orderDetails2 = new OrderDetails();
        orderDetails2.setOrdersId(orders.getId());
        orderDetails2.setProduct(productList.get(1));
        orderDetails2.setProductName(productList.get(1).getName());
        orderDetails2.setPrice(productList.get(1).getPrice());
        orderDetails2.setQuantity(2);
    }

    @Test
    void addOrders() {
        //when
        ordersJpaRepository.save(orders);

        //then
        Optional<Orders> oOrders = ordersJpaRepository.findByIdWithOrderDetails(orders.getId());
        Assertions.assertFalse(oOrders.isEmpty());
        Orders srchOrders = oOrders.get();
        assertEqualsOrders(orders, srchOrders);
    }

    @Test
    void updateOrders() {
        //when
        ordersJpaRepository.save(orders);
        orders.setAddress("주소 변경");
        orders.setStatus(Status.CANCEL);
        orders.setPayType(PayType.CARD);
        orders.setPayAmount(20000);
        orders.setCancelYn(CodeYn.Y);

        //then
        ordersJpaRepository.save(orders);

        //then
        Optional<Orders> oOrders = ordersJpaRepository.findByIdWithOrderDetails(orders.getId());
        Assertions.assertFalse(oOrders.isEmpty());
        Orders srchOrders = oOrders.get();
        assertEqualsOrders(orders, srchOrders);
    }

    @AfterEach
    public void clear() {
        ordersJpaRepository.delete(orders);
    }

    @AfterAll
    public void clearAll() {
        memberJpaRepository.delete(member);

        productJpaRepository.deleteAll(productList);
    }

    private void assertEqualsOrders(Orders o1, Orders o2) {
        Assertions.assertEquals(o1.getMember().getId(), o2.getMember().getId());
        Assertions.assertEquals(o1.getAddress(), o2.getAddress());
        Assertions.assertEquals(o1.getStatus(), o2.getStatus());
        Assertions.assertEquals(o1.getPayType(), o2.getPayType());
        Assertions.assertEquals(o1.getPayAmount(), o2.getPayAmount());
        Assertions.assertEquals(o1.getCancelYn(), o2.getCancelYn());

        Assertions.assertEquals(o1.getOrderDetails().size(), o2.getOrderDetails().size());
    }
}
