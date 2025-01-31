package com.park.mall.repository.order;

import com.park.mall.domain.order.Orders;
import com.park.mall.repository.order.dto.OrderCountStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersJpaRepository extends JpaRepository<Orders, String> {

    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.orderDetails WHERE o.id = :id")
    Optional<Orders> findByIdWithOrderDetails(@Param("id") String id);

    @Query(value = """
            SELECT
            	count(*) AS orderCount,
                pay_date AS orderDate
            FROM (
            	SELECT
            		DATE_FORMAT(pay_date,'%Y-%m-%d') AS pay_date
                FROM orders o
                WHERE 1 = 1
                AND (:status IS NULL OR o.status = :status)
                AND (:memberId IS NULL OR o.member_id = :memberId)
                AND (:startDate IS NULL OR o.pay_date >= :startDate)
                AND (:endDate IS NULL OR o.pay_date <= :endDate)
            ) t1
            GROUP BY pay_date
            """, nativeQuery = true)
    List<OrderCountStatistics> getOrderCountForDay(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("status") String status,
                                                   @Param("memberId") String memberId);
}
