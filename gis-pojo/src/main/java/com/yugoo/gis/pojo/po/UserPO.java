package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/9/20
 */
@Data
public class UserPO {
    private Integer id;
    private String name;
    private String phone;
    private String password;
    private Integer role;
    private String department;
    private Integer groupId;
    private Integer centerId;
    private String key;
    private String number;
    private String post;
    private Date ctime;

    public UserPO() {}
    public UserPO(Integer id, Integer role) {
        this.id = id;
        this.role = role;
    }
}
