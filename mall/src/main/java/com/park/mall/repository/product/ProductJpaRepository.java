package com.park.mall.repository.product;

import com.park.mall.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImgs WHERE p.id = :id")
    Optional<Product> findByIdWithProductImgs(@Param("id") Long id);
}
