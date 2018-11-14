package com.yugoo.gis.common.constant;

/**
 * @author nihao 2018/11/14
 */
public enum ResourceImportConstant {
    地市名称("cityName", String.class),
    区县("district", String.class),
    街道("streetName", String.class),
    乡镇("villageName", String.class),
    道路_行政村("admStreetName", String.class),
    片区_学校("zoneName", String.class),
    中心位置经度("longitude", Double.class),
    中心位置纬度("latitude", Double.class),
    楼层("floor", String.class),
    户号("number", String.class),
    用户场景一类("sceneA", String.class),
    用户场景二类("sceneB", String.class),
    覆盖场景("overlayScene", String.class),
    设备空余端口数("idelPortCount", Integer.class),
    端口总数("allPortCount", Integer.class),
    小区_自然村_弄("streetPOName", String.class),
    幢_号_楼("buildingNameB", String.class),
    单元号("buildingNameC", String.class);

    private String field;
    private Class clazz;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    ResourceImportConstant(String field, Class clazz) {
        this.field = field;
        this.clazz = clazz;
    }

    public static ResourceImportConstant getByName(String name) {
        for (ResourceImportConstant resourceImportConstant : ResourceImportConstant.values()) {
            if (resourceImportConstant.name().equals(name)) {
                return resourceImportConstant;
            }
        }
        return null;
    }
}
