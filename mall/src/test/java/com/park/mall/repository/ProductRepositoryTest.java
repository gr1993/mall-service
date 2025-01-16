package com.park.mall.repository;

import com.park.mall.domain.product.Product;
import com.park.mall.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Test
    void productAdd() {
        Product newProduct = new Product();
        newProduct.setName("park");
        newProduct.setPrice(1000);
        newProduct.getCreateInfo().setCreateId("manager");
        newProduct.getUpdateInfo().setModifyId("owner");

        productJpaRepository.save(newProduct);
    }
}
