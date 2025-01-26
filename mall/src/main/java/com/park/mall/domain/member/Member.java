package com.park.mall.domain.member;

import com.park.mall.domain.common.CreateInfo;
import com.park.mall.domain.common.UpdateInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Member {
    @Id
    private String id;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String email;

    @Embedded
    private CreateInfo createInfo = new CreateInfo();

    @Embedded
    private UpdateInfo updateInfo = new UpdateInfo();

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
