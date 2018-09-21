package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/9/20
 */
@Data
public class CenterPO {
    private Integer id;
    private String name;
    private Integer groupId;
    private String manager;
    private String phone;
    private String position;
    private String region;
    private Date ctime;
}
