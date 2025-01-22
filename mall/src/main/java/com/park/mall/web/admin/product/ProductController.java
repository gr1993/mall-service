package com.park.mall.web.admin.product;

import com.park.mall.service.product.ProductService;
import com.park.mall.service.product.dto.AdminProductSearch;
import com.park.mall.web.admin.common.JqGridInfo;
import com.park.mall.web.admin.product.dto.ProductRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 관리자 > 상품 관리
 */
@Controller
@RequestMapping("/admin")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public String productView() {
        return "admin/product/product";
    }

    @GetMapping("/product/list")
    @ResponseBody
    public Map<String, Object> productList(
            @ModelAttribute AdminProductSearch condition,
            @ModelAttribute JqGridInfo jqGridInfo) {

        Pageable pageable = PageRequest.of(
                jqGridInfo.getPage() - 1, // Pageable는 페이지 번호 0부터 시작
                jqGridInfo.getRows(),
                "asc".equals(jqGridInfo.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC,
                jqGridInfo.getSidx());

        return productService.searchProductForAdmin(condition, pageable);
    }

    @GetMapping("/product/register")
    public String productRegisterView(
            @RequestParam(value = "id", required = false) Long id,
            Model model) {
        if (id != null) {
            model.addAttribute("isEdit", true);
            model.addAttribute("productDetail", productService.getProductDetail(id));
        } else {
            model.addAttribute("isEdit", false);
        }
        return "admin/product/register";
    }

    /**
     * 등록, 수정을 지원하는 엔드포인트
     */
    @PostMapping("/product/register")
    @ResponseBody
    public ResponseEntity<?> productRegister(
            @Validated @RequestBody ProductRegister productRegister,
            BindingResult bindingResult
            ) {
        if (bindingResult.hasErrors()) {
            // 에러 메시지 리스트 반환
            return ResponseEntity.badRequest().body(
                    bindingResult.getFieldErrors()
                            .stream()
                            .map(error -> error.getField() + ":" + error.getDefaultMessage())
                            .toList()
            );
        }

        productService.addProduct(productRegister.convertToProduct());
        return ResponseEntity.ok().build();
    }
}
