package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/9/20
 */
@Data
public class UserInfoVO {
    private Integer id;
    private String name;
    private String phone;
    private Integer role;
    private String roleName;
    private String department;
    private Integer groupId;
    private String groupName;
    private Integer centerId;
    private String centerName;
    private String key;
    private List<MenuVO> menus;
}
