package com.park.mall.service;

import com.park.mall.domain.product.Product;
import com.park.mall.domain.product.ProductImg;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.repository.product.ProductSearchCondition;
import com.park.mall.service.file.FileService;
import com.park.mall.service.product.ProductService;
import com.park.mall.service.product.dto.AdminProductDetail;
import com.park.mall.service.product.dto.AdminProductSearch;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    private final List<Product> productList = new ArrayList<>();
    private Product product;

    @BeforeEach
    void setUp(TestInfo testInfo) throws Exception {
        if (!testInfo.getTags().contains("skipBeforeEach")) {
            InputStream inputStream = getClass().getResourceAsStream("/static/img/test.jpg");
            MockMultipartFile multipartFile = new MockMultipartFile(
                    "file", // 요청 파라미터 이름
                    "test.jpg",   // 원래 파일 이름
                    "image/jpeg", // MIME 타입
                    inputStream
            );
            String fileName = fileService.uploadTemp(multipartFile);

            product = new Product();
            product.setName("test");
            product.setPrice(2000);

            ProductImg productImg = new ProductImg();
            productImg.setProduct(product);
            productImg.setMainImgName(fileName);
            productImg.setDescImgName(fileName);
            product.getProductImgs().add(productImg);
        }
    }

    @Test
    @Transactional
    @Rollback
    void addProduct() throws Exception {
        //given
        ProductImg productImg = product.getProductImgs().get(0);

        //when
        productService.addProduct(product);

        //then
        Optional<Product> oProduct = productJpaRepository.findById(product.getId());
        Assertions.assertFalse(oProduct.isEmpty());
        Product srchProduct = oProduct.get();
        Assertions.assertEquals("test", srchProduct.getName());
        Assertions.assertEquals(2000, srchProduct.getPrice());
        ProductImg srchProductImg = srchProduct.getProductImgs().get(0);
        Assertions.assertNotNull(srchProductImg);
        Assertions.assertNotNull(srchProductImg.getMainImgPath());
        Assertions.assertNotNull(srchProductImg.getDescImgName());
        Assertions.assertEquals(productImg.getMainImgName(), srchProductImg.getMainImgName());
        Assertions.assertEquals(productImg.getDescImgName(), srchProductImg.getDescImgName());
    }

    @Test
    void updateProduct() throws Exception {
        //given
        ProductImg productImg = product.getProductImgs().get(0);
        productService.addProduct(product);

        InputStream inputStream = getClass().getResourceAsStream("/static/img/test.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", // 요청 파라미터 이름
                "test.jpg",   // 원래 파일 이름
                "image/jpeg", // MIME 타입
                inputStream
        );
        String fileName = fileService.uploadTemp(multipartFile);

        product.setName("park");
        product.setPrice(1500);
        productImg.setMainImgName(fileName);
        productImg.setDescImgName(fileName);

        //when
        productService.addProduct(product);

        //then
        Optional<Product> oProduct = productJpaRepository.findByIdWithProductImgs(product.getId());
        Assertions.assertFalse(oProduct.isEmpty());
        Product srchProduct = oProduct.get();
        Assertions.assertEquals("park", srchProduct.getName());
        Assertions.assertEquals(1500, srchProduct.getPrice());
        ProductImg srchProductImg = srchProduct.getProductImgs().get(0);
        Assertions.assertEquals(fileName, srchProductImg.getMainImgName());
        Assertions.assertEquals(fileName, srchProductImg.getDescImgName());
    }

    @Test
    @Transactional
    @Rollback
    void deleteProduct() throws Exception {
        //given
        productService.addProduct(product);

        //when
        productService.deleteProduct(product.getId());

        //then
        Optional<Product> oProduct = productJpaRepository.findById(product.getId());
        Assertions.assertTrue(oProduct.isEmpty());
    }

    @Test
    @Tag("skipBeforeEach")
    void searchProductForAdmin() {
        //given
        for(int i = 0; i < 10; i++) {
            productList.add(addProduct("park" + i, i * 1000));
        }
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Map<String, Object> response = productService.searchProductForAdmin(new AdminProductSearch(), pageable);

        //then
        Integer size = productList.size();
        Assertions.assertEquals(1, (Integer) response.get("page"));
        Assertions.assertTrue((Long) response.get("records") >= size);

        productJpaRepository.deleteAll(productList);
    }

    @Test
    void getProductDetail() {
        //given
        ProductImg productImg = product.getProductImgs().get(0);
        productService.addProduct(product);

        //when
        AdminProductDetail productDetail = productService.getProductDetail(product.getId());

        //then
        Assertions.assertNotNull(productDetail);
        Assertions.assertEquals(product.getId(), productDetail.getId());
        Assertions.assertEquals(product.getName(), productDetail.getName());
        Assertions.assertEquals(product.getPrice(), productDetail.getPrice());
        Assertions.assertEquals(productImg.getMainImgName(), productDetail.getMainImg());
        Assertions.assertEquals(productImg.getDescImgName(), productDetail.getDescImg());
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
