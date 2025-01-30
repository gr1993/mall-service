package com.park.mall.web.admin.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GridJsInfo {
    private Integer page;
    private Integer limit;
    private String colName; //sort
    private String dir;     //sort
}
