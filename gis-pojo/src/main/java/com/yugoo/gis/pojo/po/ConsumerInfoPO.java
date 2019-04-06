package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class ConsumerInfoPO {
    private Integer id;
    private Integer userId;
    private String customerName;
    private String address;
    private Integer status;
    private String phone;
    private String transactedService;
    private String transactedServiceSub;
    private String bookedService;
    private String bookedServiceSub;
    private byte[] photo;
    private String partner;
    private String remark;
    private Long transactedTime;
    private Long bookedTime;
    private Date ctime;
}
