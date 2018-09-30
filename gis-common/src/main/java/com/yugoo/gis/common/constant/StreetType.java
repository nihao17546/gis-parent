package com.yugoo.gis.common.constant;

/**
 * @author nihao 2018/9/30
 */
public enum StreetType {
    商务楼宇(1, "商务楼宇"),
    综合聚类(2, "综合聚类"),
    工业园区(3, "工业园区"),
    商业街道(4, "商业街道");
    private Integer value;
    private String name;

    StreetType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static StreetType getByValue(Integer value) {
        for (StreetType streetType : StreetType.values()) {
            if (streetType.getValue() == value) {
                return streetType;
            }
        }
        throw new RuntimeException("街道类型未找到: " + value);
    }
}
