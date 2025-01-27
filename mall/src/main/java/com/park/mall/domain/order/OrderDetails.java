package com.park.mall.domain.order;


import com.park.mall.domain.product.Product;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="orders_id")
    private Orders orders;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    @Column
    private String productName;

    @Column
    private Integer price;

    @Column
    private Integer quantity;
}
