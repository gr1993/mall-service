package com.park.mall.service.product;

import com.park.mall.domain.product.Product;
import com.park.mall.domain.product.ProductImg;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.repository.product.ProductQueryRepository;
import com.park.mall.repository.product.ProductSearchCondition;
import com.park.mall.service.file.FileService;
import com.park.mall.service.product.dto.AdminProductDetail;
import com.park.mall.service.product.dto.AdminProductInfo;
import com.park.mall.service.product.dto.AdminProductSearch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private MessageSource messageSource;

    public void addProduct(Product product) {
        // 등록
        if (product.getId() == null) {
            ProductImg productImg = product.getProductImgs().get(0);

            //임시 경로에서 실제 업로드 처리하기
            String mainImgPath = fileService.uploadFromTemp(productImg.getMainImgName());
            productImg.setMainImgPath(mainImgPath);
            if (StringUtils.hasText(productImg.getDescImgName())) {
                String descImgPath = fileService.uploadFromTemp(productImg.getDescImgName());
                productImg.setDescImgPath(descImgPath);
            }

            productJpaRepository.save(product);
        }
        // 수정
        else {
            Optional<Product> oProduct = productJpaRepository.findByIdWithProductImgs(product.getId());
            if (oProduct.isEmpty()) {
                String errorMessage = messageSource.getMessage("product.NotFound", null, Locale.getDefault());
                throw new IllegalArgumentException(errorMessage);
            }

            ProductImg productImg = product.getProductImgs().get(0);
            Product srchProduct = oProduct.get();
            ProductImg srchProductImg =  srchProduct.getProductImgs().get(0);
            // 상품이미지가 변경되었으면 업로드 처리
            if (!srchProductImg.getMainImgName().equals(productImg.getMainImgName())) {
                String mainImgPath = fileService.uploadFromTemp(productImg.getMainImgName());
                srchProductImg.setMainImgName(productImg.getMainImgName());
                srchProductImg.setMainImgPath(mainImgPath);
            }
            srchProduct.setName(product.getName());
            srchProduct.setPrice(product.getPrice());
            // 상세이미지가 변경되었으면 업로드 처리
            if (!srchProductImg.getDescImgName().equals(productImg.getDescImgName())) {
                String descImgPath = fileService.uploadFromTemp(productImg.getDescImgName());
                srchProductImg.setDescImgName(productImg.getDescImgName());
                srchProductImg.setDescImgPath(descImgPath);
            }

            productJpaRepository.save(srchProduct);
        }
    }

    public void deleteProduct(Long id) {
        productJpaRepository.deleteById(id);
    }

    public Map<String, Object> searchProductForAdmin(AdminProductSearch condition, Pageable pageable) {
        ProductSearchCondition searchCondition = new ProductSearchCondition();
        searchCondition.setId(condition.getProductId());
        searchCondition.setName(condition.getProductName());
        searchCondition.setMinPrice(condition.getMinPrice());
        searchCondition.setMaxPrice(condition.getMaxPrice());

        Page<Product> page = productQueryRepository.searchPage(searchCondition, pageable);
        List<Product> content = page.getContent();
        List<AdminProductInfo> productList = new ArrayList<>();
        for(Product p : content) {
            productList.add(new AdminProductInfo(p));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("page", page.getNumber() + 1); // jqGrid는 페이지 번호 1부터 시작
        response.put("total", page.getTotalPages()); // 페이지 총 수
        response.put("records", page.getTotalElements()); // 레코드 총 수
        response.put("rows", productList); // 데이터
        return response;
    }

    public AdminProductDetail getProductDetail(Long id) {
        Optional<Product> oProduct = productJpaRepository.findByIdWithProductImgs(id);
        if (oProduct.isEmpty()) {
            return null;
        }

        Product product = oProduct.get();
        ProductImg productImg = product.getProductImgs().get(0);
        AdminProductDetail productDetail = new AdminProductDetail();
        productDetail.setId(product.getId());
        productDetail.setName(product.getName());
        productDetail.setPrice(product.getPrice());
        productDetail.setMainImg(productImg.getMainImgName());
        productDetail.setMainPath(productImg.getMainImgPath());
        productDetail.setDescImg(productImg.getDescImgName());
        productDetail.setDescPath(productImg.getDescImgPath());
        return productDetail;
    }
}
