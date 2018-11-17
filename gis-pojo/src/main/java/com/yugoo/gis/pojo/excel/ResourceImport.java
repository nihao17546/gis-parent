package com.yugoo.gis.pojo.excel;

import lombok.Data;

/**
 * @author nihao 2018/11/14
 */
@Data
public class ResourceImport {

    @ExcelImport("地市名称")
    private String cityName;

    @ExcelImport("区县")
    private String district;

    @ExcelImport("街道")
    private String streetName;

    @ExcelImport("乡镇")
    private String villageName;

    @ExcelImport("道路/行政村")
    private String admStreetName;

    @ExcelImport("片区/学校")
    private String zoneName;

    @ExcelImport("中心位置经度")
    private Double longitude;

    @ExcelImport("中心位置纬度")
    private Double latitude;

    @ExcelImport("楼层")
    private String floor;

    @ExcelImport("户号")
    private String number;

    @ExcelImport("用户场景一类")
    private String sceneA;

    @ExcelImport("用户场景二类")
    private String sceneB;

    @ExcelImport("覆盖场景")
    private String overlayScene;

    @ExcelImport({"空闲端口数","设备空余端口数"})
    private Integer allPortCount;

    @ExcelImport({"总端口数","端口总数"})
    private Integer idelPortCount;

    @ExcelImport("小区/自然村/弄")
    private String streetPOName;

    @ExcelImport("幢/号/楼")
    private String buildingNameB;

    @ExcelImport("单元号")
    private String buildingNameC;

    private Integer buildingId;
    private String buildingPOName;
    private Integer row;

    public String getR() {
        return "第" + row + "行";
    }
}
