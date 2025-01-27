package com.park.mall.domain.order;

import com.park.mall.domain.common.CodeYn;
import com.park.mall.domain.member.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String address;

    @Convert(converter = StatusConverter.class)
    private Status status;

    @Convert(converter = PayTypeConverter.class)
    private PayType payType;

    @Column
    private Integer payAmount;

    @Column
    private LocalDateTime payDate;

    @Enumerated(EnumType.STRING)
    private CodeYn cancelYn;

    @Column
    private LocalDateTime cancelDate;

    @OneToMany(
            mappedBy = "orders",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private List<OrderDetails> orderDetails = new ArrayList<>();
}
