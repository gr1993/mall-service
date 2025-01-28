package com.park.mall.domain.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderDetails is a Querydsl query type for OrderDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderDetails extends EntityPathBase<OrderDetails> {

    private static final long serialVersionUID = -36370309L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderDetails orderDetails = new QOrderDetails("orderDetails");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOrders orders;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.park.mall.domain.product.QProduct product;

    public final StringPath productName = createString("productName");

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QOrderDetails(String variable) {
        this(OrderDetails.class, forVariable(variable), INITS);
    }

    public QOrderDetails(Path<? extends OrderDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderDetails(PathMetadata metadata, PathInits inits) {
        this(OrderDetails.class, metadata, inits);
    }

    public QOrderDetails(Class<? extends OrderDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orders = inits.isInitialized("orders") ? new QOrders(forProperty("orders"), inits.get("orders")) : null;
        this.product = inits.isInitialized("product") ? new com.park.mall.domain.product.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

