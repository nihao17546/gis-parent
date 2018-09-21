package com.yugoo.gis.common.constant;

/**
 * @author nihao 2018/9/20
 */
public enum Role {
    admin(1),headman(2),member(3);
    private Integer value;

    Role(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static Role getByValue(Integer value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new RuntimeException("角色为找到: " + value);
    }
}
