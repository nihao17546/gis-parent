package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author nihao 2018/10/9
 */
@Data
public class ConsumerPO {
    private Integer id;
    private String name;
    private Integer buildingId;
    private String floor;
    private String number;
    private String position;
    private byte[] pic;
    private String category;
    private String nature;
    private Integer peopleNum;
    private String linkman;
    private String phone;
    private String operator;
    private BigDecimal expenses;
    private Long expirationDate;
    private String bandwidth;
    private Integer serviceType;
    private String status;
    private String legal;
    private Integer lineNum;
    private String lineType;
    private Long lineOpenDate;
    private String lineStatus;
    private String expensesName;
    private Long orderTime;
    private String memberRole;
    private String memberRoleRealNum;
    private String memberExpensesName;
    private String groupCode;
    private String groupGrade;
    private Integer userId;
    private Integer type;
    private Double longitude;
    private Double latitude;
    private Date ctime;
}
