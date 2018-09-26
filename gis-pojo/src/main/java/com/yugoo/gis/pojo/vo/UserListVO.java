package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/9/20
 */
@Data
public class UserListVO {
    private Integer id;
    private String name;
    private String phone;
    private Integer role;
    private String department;
    private Integer groupId;
    private Integer centerId;
    private String key;
    private Date ctime;

    private String roleName;
    private String groupName;
    private String centerName;

}
