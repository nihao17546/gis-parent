package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/9/26
 */
@Data
public class MenuVO {
    private String name;
    private String path;
    private List<MenuVO> children;
    private String target;
    private Integer index;
    private String icon;
}
