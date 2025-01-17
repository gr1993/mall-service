package com.park.mall.web.admin.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JqGridInfo {
    private Integer page;
    private Integer rows;
    private String sidx;
    private String sord;
}
