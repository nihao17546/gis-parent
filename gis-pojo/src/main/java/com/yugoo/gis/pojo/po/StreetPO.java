package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/9/30
 */
@Data
public class StreetPO {
    private Integer id;
    private String name;
    private String position;
    private Integer type;
    private String manager;
    private String phone;
    private byte[] pic;
    private String remark;
    private String competitor;
    private Date ctime;
}
