package com.park.mall.domain.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrders is a Querydsl query type for Orders
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrders extends EntityPathBase<Orders> {

    private static final long serialVersionUID = 1014199692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrders orders = new QOrders("orders");

    public final StringPath address = createString("address");

    public final DateTimePath<java.time.LocalDateTime> cancelDate = createDateTime("cancelDate", java.time.LocalDateTime.class);

    public final EnumPath<com.park.mall.domain.common.CodeYn> cancelYn = createEnum("cancelYn", com.park.mall.domain.common.CodeYn.class);

    public final StringPath id = createString("id");

    public final com.park.mall.domain.member.QMember member;

    public final ListPath<OrderDetails, QOrderDetails> orderDetails = this.<OrderDetails, QOrderDetails>createList("orderDetails", OrderDetails.class, QOrderDetails.class, PathInits.DIRECT2);

    public final NumberPath<Integer> payAmount = createNumber("payAmount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> payDate = createDateTime("payDate", java.time.LocalDateTime.class);

    public final EnumPath<PayType> payType = createEnum("payType", PayType.class);

    public final StringPath receiptId = createString("receiptId");

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public QOrders(String variable) {
        this(Orders.class, forVariable(variable), INITS);
    }

    public QOrders(Path<? extends Orders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrders(PathMetadata metadata, PathInits inits) {
        this(Orders.class, metadata, inits);
    }

    public QOrders(Class<? extends Orders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.park.mall.domain.member.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

