package com.park.mall.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCreateInfo is a Querydsl query type for CreateInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCreateInfo extends BeanPath<CreateInfo> {

    private static final long serialVersionUID = -1441679290L;

    public static final QCreateInfo createInfo = new QCreateInfo("createInfo");

    public final DateTimePath<java.time.LocalDateTime> createdDtm = createDateTime("createdDtm", java.time.LocalDateTime.class);

    public final StringPath createId = createString("createId");

    public QCreateInfo(String variable) {
        super(CreateInfo.class, forVariable(variable));
    }

    public QCreateInfo(Path<? extends CreateInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCreateInfo(PathMetadata metadata) {
        super(CreateInfo.class, metadata);
    }

}

