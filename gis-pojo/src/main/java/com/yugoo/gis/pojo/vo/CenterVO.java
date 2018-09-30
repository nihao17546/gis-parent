package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author nihao 2018/9/28
 */
@Data
public class CenterVO {
    private Integer id;
    private String name;
    private Integer groupId;
    private String manager;
    private String phone;
    private String position;
    private String district;
    private String region;
    private Date ctime;

    private String groupName;
    private List<PointVO> points;
}
