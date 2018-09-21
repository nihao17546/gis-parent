package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/9/20
 */
@Data
public class GroupPO {
    private Integer id;
    private String name;
    private String position;
    private Date ctime;
}
