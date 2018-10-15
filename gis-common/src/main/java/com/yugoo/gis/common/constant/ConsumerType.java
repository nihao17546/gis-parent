package com.yugoo.gis.common.constant;

/**
 * @author nihao
 * @create 2018/10/13
 **/
public enum  ConsumerType {
    未建档(1, "未建档"),
    基础建档(2, "基础建档"),
    有效建档(3, "有效建档");
    private Integer value;
    private String name;

    ConsumerType(Integer value, String name) {
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

    public static ConsumerType getByValue(Integer value) {
        for (ConsumerType consumerType : ConsumerType.values()) {
            if (consumerType.getValue() == value) {
                return consumerType;
            }
        }
        throw new RuntimeException("建档类型未找到: " + value);
    }
}
