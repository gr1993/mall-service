package com.park.mall.service.product;

import com.park.mall.domain.product.Product;
import com.park.mall.repository.product.ProductJpaRepository;
import com.park.mall.repository.product.ProductQueryRepository;
import com.park.mall.repository.product.ProductSearchCondition;
import com.park.mall.service.product.dto.AdminProductInfo;
import com.park.mall.service.product.dto.AdminProductSearch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
