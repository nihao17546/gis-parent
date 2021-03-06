package com.yugoo.gis.pojo.vo;

import lombok.Data;

/**
 * @author nihao 2018/9/20
 */
@Data
public class UserInfoVO {
    private Integer id;
    private String name;
    private String phone;
    private Integer role;
    private String department;
    private Integer groupId;
    private Integer centerId;
    private String key;
}
