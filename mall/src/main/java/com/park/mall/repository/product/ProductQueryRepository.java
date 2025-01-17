package com.park.mall.repository.product;

import com.park.mall.domain.product.Product;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.park.mall.domain.product.QProduct.product;

@Repository
public class ProductQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public ProductQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Product> searchAll(ProductSearchCondition condition) {
        return query
                .select(product)
                .from(product)
                .where(
                        conditions(condition)
                )
                .fetch();
    }

    public Page<Product> searchPage(ProductSearchCondition condition, Pageable pageable) {
        List<Product> fetch = query
                .select(product)
                .from(product)
                .where(
                        conditions(condition)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = query
                .select(product.count())
                .from(product)
                .where(
                        conditions(condition)
                );

        return PageableExecutionUtils.getPage(fetch, pageable, count::fetchOne);
    }

    private BooleanExpression[] conditions (ProductSearchCondition condition) {
        return new BooleanExpression[] {
                equalId(condition.getId()),
                likeName(condition.getName()),
                maxPrice(condition.getMaxPrice()),
                minPrice(condition.getMinPrice())
        };
    }

    private BooleanExpression equalId(Long id) {
        if (id != null) {
            return product.id.eq(id);
        }
        return null;
    }

    private BooleanExpression likeName(String name) {
        if (StringUtils.hasText(name)) {
            return product.name.like("%" + name + "%");
        }
        return null;
    }

    private BooleanExpression maxPrice(Integer price) {
        if (price != null) {
            return product.price.loe(price);
        }
        return null;
    }

    private BooleanExpression minPrice(Integer price) {
        if (price != null) {
            return product.price.goe(price);
        }
        return null;
    }
}
