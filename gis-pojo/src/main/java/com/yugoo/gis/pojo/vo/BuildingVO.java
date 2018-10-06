package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao
 * @create 2018/10/5
 **/
@Data
public class BuildingVO {
    private Integer id;
    private String name;
    private Integer streetId;
    private Double longitude;
    private Double latitude;
    private Date ctime;

    private String streetName;
}
