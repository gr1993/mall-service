package com.park.mall.domain.product;

import com.park.mall.domain.common.CreateInfo;
import com.park.mall.domain.common.UpdateInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Column
    private String mainImgName;

    @Column
    private String mainImgPath;

    @Column
    private String descImgName;

    @Column
    private String descImgPath;

    @Embedded
    private CreateInfo createInfo = new CreateInfo();

    @PrePersist
    public void prePersist() {
        this.createInfo.setCreatedDtm(LocalDateTime.now());
    }
}
