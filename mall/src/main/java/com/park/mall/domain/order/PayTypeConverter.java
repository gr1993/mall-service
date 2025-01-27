package com.park.mall.domain.order;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PayTypeConverter implements AttributeConverter<PayType, String> {

    @Override
    public String convertToDatabaseColumn(PayType payType) {
        if (payType == null) return null;
        return payType.getCode();
    }

    @Override
    public PayType convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return PayType.fromCode(dbData);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
}
