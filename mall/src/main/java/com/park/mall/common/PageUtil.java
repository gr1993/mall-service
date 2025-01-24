package com.park.mall.common;

import com.park.mall.common.dto.PageInfo;

public class PageUtil {

    public static PageInfo getPageInfo(Integer totalPages, Integer page) {
        PageInfo pageInfo = new PageInfo();
        if (totalPages > 5) {
            if (page - 2 < 1) {
                pageInfo.setFirstPage(1);
                pageInfo.setLastPage(5);
            }
            else if (page + 2 > totalPages) {
                pageInfo.setFirstPage(totalPages - 4);
                pageInfo.setLastPage(totalPages);
            }
            else {
                pageInfo.setFirstPage(page - 2);
                pageInfo.setLastPage(page + 2);
            }
        } else {
            pageInfo.setFirstPage(1);
            pageInfo.setLastPage(totalPages);
        }
        return pageInfo;
    }
}
