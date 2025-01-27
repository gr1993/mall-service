package com.park.mall.domain.order;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        if (status == null) return null;
        return status.getCode();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return Status.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
