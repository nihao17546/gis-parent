package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/10/23
 */
@Data
public class ResourcePO {
    private Integer id;
    private Integer buildingId;
    private String district;
    private String floor;
    private String number;
    private Integer allPortCount;
    private Integer idelPortCount;
    private String sceneA;
    private String sceneB;
    private String overlayScene;
    private Double longitude;
    private Double latitude;
    private Date ctime;
}
