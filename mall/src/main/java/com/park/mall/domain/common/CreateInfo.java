package com.park.mall.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Embeddable
public class CreateInfo {

    @Column
    private LocalDateTime createdDtm;

    @Column
    private String createId;
}
