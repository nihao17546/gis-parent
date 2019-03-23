package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2019/3/22
 */
@Data
public class NoticePO {
    private Integer id;
    private String title;
    private String content;
    private Integer sorting;
    private Date ctime;
}
