package com.yugoo.gis.common.constant;

/**
 * @author nihao 2018/9/20
 */
public enum Role {
    admin(1, "管理员"),headman(2, "要客组长"),member(3, "客户经理");
    private Integer value;
    private String name;

    Role(Integer value, String name) {
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

    public static Role getByValue(Integer value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new RuntimeException("角色未找到: " + value);
    }
}
