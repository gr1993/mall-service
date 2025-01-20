package com.park.mall.repository;

import com.park.mall.domain.product.Product;
import com.park.mall.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductJpaRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private List<Product> productList = new ArrayList<>();
    private Product newProduct;

    @BeforeEach
    public void setup() {
        newProduct = new Product();
        newProduct.setName("park");
        newProduct.setPrice(1000);
        newProduct.getCreateInfo().setCreateId("manager");
        newProduct.getUpdateInfo().setModifyId("owner");
        productList.add(newProduct);
    }

    @Test
    void addProduct() {
        //when
        productJpaRepository.save(newProduct);

        //then
        Product product = selectProductById(newProduct.getId());
        assertProduct(newProduct, product);
    }

    @Test
    void updateProduct() {
        //given
        productJpaRepository.save(newProduct);
        newProduct.setName("lim");
        newProduct.setPrice(2000);
        newProduct.getCreateInfo().setCreateId("create");
        newProduct.getUpdateInfo().setModifyId("update");

        //when
        productJpaRepository.save(newProduct);

        //then
        Product product = selectProductById(newProduct.getId());
        assertProduct(newProduct, product);
    }

    @Test
    void deleteProduct() {
        //given
        productJpaRepository.save(newProduct);

        //when
        productJpaRepository.deleteById(newProduct.getId());

        //then
        Optional<Product> oProduct = productJpaRepository.findById(newProduct.getId());
        Assertions.assertTrue(oProduct.isEmpty());
    }

    @AfterEach
    public void clear() {
        productJpaRepository.deleteAll(productList);
    }

    private Product selectProductById(Long id) {
        Optional<Product> oProduct = productJpaRepository.findById(id);
        Assertions.assertFalse(oProduct.isEmpty());
        return oProduct.get();
    }

    private void assertProduct(Product p1, Product p2) {
        Assertions.assertEquals(p1.getName(), p2.getName());
        Assertions.assertEquals(p1.getPrice(), p2.getPrice());
        Assertions.assertEquals(p1.getCreateInfo().getCreateId(), p2.getCreateInfo().getCreateId());
        Assertions.assertEquals(p1.getUpdateInfo().getModifyId(), p2.getUpdateInfo().getModifyId());
    }
}
