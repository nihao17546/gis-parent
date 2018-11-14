package com.yugoo.gis.pojo.excel;

import lombok.Data;

/**
 * @author nihao 2018/11/14
 */
@Data
public class ResourceImport {
    private Integer buildingId;
    private String district;
    private String cityName;
    private String streetName;
    private String villageName;
    private String admStreetName;
    private String zoneName;
    private String floor;
    private String number;
    private Integer allPortCount;
    private Integer idelPortCount;
    private String sceneA;
    private String sceneB;
    private String overlayScene;
    private Double longitude;
    private Double latitude;

    private String buildingPOName;
    private String streetPOName;

    private String buildingNameB;
    private String buildingNameC;

    private String row;
}
