package com.park.mall.repository.order;

import com.park.mall.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrdersJpaRepository extends JpaRepository<Orders, String> {

    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.orderDetails WHERE o.id = :id")
    Optional<Orders> findByIdWithOrderDetails(@Param("id") String id);
}
