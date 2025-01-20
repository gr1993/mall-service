package com.park.mall.service.product;

import com.park.mall.domain.product.Product;
import com.park.mall.domain.product.ProductImg;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.repository.product.ProductQueryRepository;
import com.park.mall.repository.product.ProductSearchCondition;
import com.park.mall.service.file.FileService;
import com.park.mall.service.product.dto.AdminProductInfo;
import com.park.mall.service.product.dto.AdminProductSearch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

    @Autowired
    private FileService fileService;

    public void addProduct(Product product) {
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
}
