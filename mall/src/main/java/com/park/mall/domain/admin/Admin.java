package com.park.mall.domain.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Admin {

    @Id
    private String id;

    @Column
    private String password;

    @Column
    private String name;
}
