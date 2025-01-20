package com.park.mall.repository;

import com.park.mall.domain.product.Product;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.repository.product.ProductQueryRepository;
import com.park.mall.repository.product.ProductSearchCondition;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ProductQueryRepositoryTest {

    @Autowired
    private ProductQueryRepository productQueryRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private final int setupSize = 50;
    private final List<Product> productList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        //productJpaRepository.deleteAll();
        for(int i = 0; i < setupSize; i++) {
            productList.add(addProduct("park" + i, i * 1000));
        }
    }

    @Test
    void searchAll() {
        //given
        int size = productList.size();

        //when
        List<Product> searchList = productQueryRepository.searchAll(new ProductSearchCondition());

        //then
        Assertions.assertTrue(searchList.size() >= size);


        //given
        ProductSearchCondition condition = new ProductSearchCondition();
        condition.setId(productList.get(0).getId());

        //when
        searchList = productQueryRepository.searchAll(condition);

        //then
        Assertions.assertEquals(1, searchList.size());
        Assertions.assertEquals(productList.get(0).getId(), searchList.get(0).getId());


        //given
        condition = new ProductSearchCondition();
        condition.setMinPrice(1000);
        condition.setMaxPrice(10000);

        //when
        searchList = productQueryRepository.searchAll(condition);

        Assertions.assertTrue(searchList.size() >= 10);
    }

    @Test
    void searchPage() {
        //given
        Pageable pageable = PageRequest.of(1, 10);

        //when
        Page<Product> productPage = productQueryRepository.searchPage(new ProductSearchCondition(), pageable);

        //then
        long total = productPage.getTotalElements();
        Assertions.assertTrue(total >= setupSize);
        Assertions.assertEquals(10, productPage.getContent().size());
    }

    @AfterEach
    public void clear() {
        productJpaRepository.deleteAll(productList);
    }

    private Product addProduct(String name, Integer price) {
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setPrice(price);
        newProduct.getCreateInfo().setCreateId("manager");
        newProduct.getUpdateInfo().setModifyId("owner");
        return productJpaRepository.save(newProduct);
    }
}
