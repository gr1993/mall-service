package com.park.mall.common;

import com.park.mall.common.dto.PageInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageUtilTest {

    @Test
    public void getPageInfoOnlyOne() {
        //when
        PageInfo pageInfo = PageUtil.getPageInfo(1, 1);

        //then
        Assertions.assertEquals(1, pageInfo.getFirstPage());
        Assertions.assertEquals(1, pageInfo.getLastPage());
    }

    @Test
    public void getPageInfoTotalFive() {
        //when
        PageInfo pageInfo = PageUtil.getPageInfo(5, 1);

        //then
        Assertions.assertEquals(1, pageInfo.getFirstPage());
        Assertions.assertEquals(5, pageInfo.getLastPage());
    }

    @Test
    public void getPageInfoTotalFiveOther() {
        //when
        PageInfo pageInfo = PageUtil.getPageInfo(5, 4);

        //then
        Assertions.assertEquals(1, pageInfo.getFirstPage());
        Assertions.assertEquals(5, pageInfo.getLastPage());
    }

    @Test
    public void getPageInfoTotalNine() {
        //when
        PageInfo pageInfo = PageUtil.getPageInfo(9, 5);

        //then
        Assertions.assertEquals(3, pageInfo.getFirstPage());
        Assertions.assertEquals(7, pageInfo.getLastPage());
    }
}
