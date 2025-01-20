package com.park.mall.domain.product;

import com.park.mall.domain.common.CreateInfo;
import com.park.mall.domain.common.UpdateInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer price;

    @Embedded
    private CreateInfo createInfo = new CreateInfo();

    @Embedded
    private UpdateInfo updateInfo = new UpdateInfo();

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<ProductImg> productImgs = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createInfo.setCreatedDtm(LocalDateTime.now());
        this.updateInfo.setModifiedDtm(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.updateInfo.setModifiedDtm(LocalDateTime.now());
    }
}
