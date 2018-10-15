package com.yugoo.gis.common.constant;

/**
 * @author nihao
 * @create 2018/10/11
 **/
public enum ServiceType {
    专线产品(1, "专线产品"),
    酒店产品(2, "酒店产品"),
    商务动力(3, "商务动力");
    private Integer value;
    private String name;

    ServiceType(Integer value, String name) {
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

    public static ServiceType getByValue(Integer value) {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceType.getValue() == value) {
                return serviceType;
            }
        }
        throw new RuntimeException("业务类型未找到: " + value);
    }
}
