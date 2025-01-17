package com.park.mall.domain.common;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUpdateInfo is a Querydsl query type for UpdateInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUpdateInfo extends BeanPath<UpdateInfo> {

    private static final long serialVersionUID = 1188143507L;

    public static final QUpdateInfo updateInfo = new QUpdateInfo("updateInfo");

    public final DateTimePath<java.time.LocalDateTime> modifiedDtm = createDateTime("modifiedDtm", java.time.LocalDateTime.class);

    public final StringPath modifyId = createString("modifyId");

    public QUpdateInfo(String variable) {
        super(UpdateInfo.class, forVariable(variable));
    }

    public QUpdateInfo(Path<? extends UpdateInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUpdateInfo(PathMetadata metadata) {
        super(UpdateInfo.class, metadata);
    }

}

