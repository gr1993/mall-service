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
    private String id;

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
    private CodeYn cancelYn = CodeYn.N;

    @Column
    private LocalDateTime cancelDate;

    @Column
    private String receiptId;

    /**
     * @Id만 지정한 엔티티인 경우는 CascadeType.MERGE도 포함하여야 PERSIST 저장 가능
     * 참고 자료 : https://browngoo.tistory.com/4
     */
    @OneToMany(
            mappedBy = "orders",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    private List<OrderDetails> orderDetails = new ArrayList<>();
}
