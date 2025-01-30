package com.park.mall.repository.order;

import com.park.mall.domain.order.Orders;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.park.mall.domain.order.QOrders.orders;
import static com.park.mall.domain.order.QOrderDetails.orderDetails;
import static com.park.mall.domain.member.QMember.member;
import static com.park.mall.domain.product.QProduct.product;
import static com.park.mall.domain.product.QProductImg.productImg;

@Repository
public class OrdersQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrdersQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public Page<Orders> searchPage(OrderSearchCondition condition, Pageable pageable) {
        return searchPage(condition, pageable, false);
    }

    public Page<Orders> searchPage(OrderSearchCondition condition, Pageable pageable, boolean fetchJoin) {
        JPAQuery<Orders> fetchQuery = query
                .select(orders)
                .from(orders)
                .leftJoin(orders.member, member).fetchJoin();

        if (fetchJoin) {
            fetchQuery.leftJoin(orders.orderDetails, orderDetails).fetchJoin()
                    .leftJoin(orderDetails.product, product).fetchJoin();
        }

        fetchQuery.where(
                        conditions(condition)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        pageable.getSort().stream().forEach(sort -> {
            Order order = sort.isAscending() ? Order.ASC : Order.DESC;
            String property = sort.getProperty();

            Path<Object> target = Expressions.path(Object.class, orders, property);
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier(order, target);
            fetchQuery.orderBy(orderSpecifier);
        });

        // count 쿼리에는 join된 member가 select에 반영할 필요가 없으므로
        // fetchJoin을 호출하여서는 안된다.
        JPAQuery<Long> count = query
                .select(orders.count())
                .from(orders)
                .leftJoin(orders.member, member)
                .where(
                        conditions(condition)
                );

        return PageableExecutionUtils.getPage(fetchQuery.fetch(), pageable, count::fetchOne);
    }

    private BooleanExpression[] conditions (OrderSearchCondition condition) {
        return new BooleanExpression[] {
                equalId(condition.getId()),
                equalMemberId(condition.getMemberId())
        };
    }

    private BooleanExpression equalId(String id) {
        if (StringUtils.hasText(id)) {
            return orders.id.eq(id);
        }
        return null;
    }

    private BooleanExpression equalMemberId(String memberId) {
        if (StringUtils.hasText(memberId)) {
            return member.id.eq(memberId);
        }
        return null;
    }
}

