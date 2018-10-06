package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author nihao 2018/9/30
 */
@Data
public class StreetVO {
    private Integer id;
    private String name;
    private String position;
    private Integer type;
    private String manager;
    private String phone;
    private String remark;
    private byte[] pic;
    private Date ctime;

    private String typeName;
    private List<CompetitorVO> competitors;
    private List<PointVO> buildingPoints;
}
