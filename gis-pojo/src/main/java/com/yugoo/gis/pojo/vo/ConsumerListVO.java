package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author nihao
 * @create 2018/10/11
 **/
@Data
public class ConsumerListVO {
    private Integer id;
    private String name;
    private Integer buildingId;
    private String floor;
    private String number;
    private String position;
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
    private String groupCode;
    private String groupGrade;
    private Integer userId;
    private Integer type;
    private Date ctime;

    private String typeName;
    private String buildingName;
    private String serviceTypeName;
    private String expirationDateStr;
    private String lineOpenDateStr;
    private String userName;
}
