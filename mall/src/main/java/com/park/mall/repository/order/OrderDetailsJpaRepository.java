package com.park.mall.repository.order;

import com.park.mall.domain.order.OrderDetails;
import com.park.mall.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailsJpaRepository extends JpaRepository<OrderDetails, Long> {
}
