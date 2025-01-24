package com.park.mall.web.mall.product;

import com.park.mall.common.PageUtil;
import com.park.mall.common.dto.PageInfo;
import com.park.mall.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MallProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/list")
    public String productView(
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            Model model
    ) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 9;
        }

        Pageable pageable = PageRequest.of(
                page - 1, // Pageable는 페이지 번호 0부터 시작
                pageSize,
                Sort.Direction.DESC,
                "id");

        Map<String, Object> response = productService.searchProduct(searchText, pageable);

        // 페이지네이션 설정 계산
        Integer totalPages = (Integer)response.get("totalPages");
        PageInfo pageInfo = PageUtil.getPageInfo(totalPages, page);

        model.addAttribute("searchText", searchText);
        model.addAttribute("page", response.get("page"));
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("firstPage", pageInfo.getFirstPage());
        model.addAttribute("lastPage", pageInfo.getLastPage());
        model.addAttribute("totalElements", response.get("totalElements"));
        model.addAttribute("productList", response.get("data"));
        return "mall/product/list";
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "mall/product/detail";
    }
}
