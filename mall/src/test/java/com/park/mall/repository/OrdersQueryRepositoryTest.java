package com.park.mall.repository;

import com.park.mall.common.IdUtil;
import com.park.mall.domain.common.CodeYn;
import com.park.mall.domain.member.Member;
import com.park.mall.domain.order.OrderDetails;
import com.park.mall.domain.order.Orders;
import com.park.mall.domain.order.PayType;
import com.park.mall.domain.order.Status;
import com.park.mall.domain.product.Product;
import com.park.mall.domain.product.ProductImg;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.repository.order.dto.OrderSearchCondition;
import com.park.mall.repository.order.OrdersJpaRepository;
import com.park.mall.repository.order.OrdersQueryRepository;
import com.park.mall.repository.order.dto.ProductCountStatistics;
import com.park.mall.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrdersQueryRepositoryTest {

    @Autowired
    private OrdersQueryRepository ordersQueryRepository;

    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private Member member;

    private final List<Product> productList = new ArrayList<>();
    private List<Orders> ordersList;

    @BeforeAll
    public void setupAll() {
        member = new Member();
        member.setId("ordersQueryTest");
        member.setName("테스터");
        member.setEmail("test@naver.com");
        memberJpaRepository.save(member);

        for (int i = 1; i <= 2; i++) {
            Product product = new Product();
            product.setName("상품" + i);
            product.setPrice(1000 * i);

            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);
            productImg.setMainImgName("mainImgName");
            productImg.setMainImgPath("mainImgPath");
            product.getProductImgs().add(productImg);

            productJpaRepository.save(product);
            productList.add(product);
        }
    }

    @BeforeEach
    @Transactional
    public void setup() {
        ordersList = new ArrayList<>();

        for (int i = 1; i <= 4; i++) {
            Orders orders = new Orders();
            orders.setId(IdUtil.generateOrderId() + i);
            orders.setMember(member);
            orders.setAddress("서울시 구로구 디지털로 00길 00");
            orders.setStatus(Status.PREPARE);
            orders.setPayType(PayType.CARD);
            orders.setPayAmount(10000);
            orders.setPayDate(LocalDateTime.now());
            orders.setCancelYn(CodeYn.N);
            orders.setReceiptId("abcd123" + i);

            OrderDetails orderDetails1 = new OrderDetails();
            orderDetails1.setOrders(orders);
            orderDetails1.setProduct(productList.get(0));
            orderDetails1.setProductName(productList.get(0).getName());
            orderDetails1.setPrice(productList.get(0).getPrice());
            orderDetails1.setQuantity(1);

            OrderDetails orderDetails2 = new OrderDetails();
            orderDetails2.setOrders(orders);
            orderDetails2.setProduct(productList.get(1));
            orderDetails2.setProductName(productList.get(1).getName());
            orderDetails2.setPrice(productList.get(1).getPrice());
            orderDetails2.setQuantity(2);
            orders.getOrderDetails().add(orderDetails1);
            orders.getOrderDetails().add(orderDetails2);

            ordersList.add(orders);
        }
        ordersJpaRepository.saveAll(ordersList);
    }

    @Test
    @Transactional
    void searchPage() {
        //given
        OrderSearchCondition orderSearchCondition = new OrderSearchCondition();
        orderSearchCondition.setMemberId(member.getId());
        Pageable pageable = PageRequest.of(0, 3, Sort.Direction.DESC, "id");

        //when
        Page<Orders> ordersPage = ordersQueryRepository.searchPage(orderSearchCondition, pageable, true);

        //then
        long total = ordersPage.getTotalElements();
        Assertions.assertEquals(4, total);
        Assertions.assertEquals(3, ordersPage.getContent().size());

        List<Orders> contentList = ordersPage.getContent();
        OrderDetails orderDetails = contentList.get(0).getOrderDetails().get(0);
        Assertions.assertNotNull(orderDetails);

        ProductImg productImg = orderDetails.getProduct().getProductImgs().get(0);
        Assertions.assertNotNull(productImg);
    }

    @Test
    @Transactional
    void getOrderProductStat() {
        //when
        List<ProductCountStatistics> orderProductStatList = ordersQueryRepository.getOrderProductStat();

        //then
        Assertions.assertFalse(orderProductStatList.isEmpty());
    }

    @AfterEach
    @Transactional
    public void clear() {
        ordersJpaRepository.deleteAll(ordersList);
    }

    @AfterAll
    public void clearAll() {
        memberJpaRepository.delete(member);

        productJpaRepository.deleteAll(productList);
    }
}
