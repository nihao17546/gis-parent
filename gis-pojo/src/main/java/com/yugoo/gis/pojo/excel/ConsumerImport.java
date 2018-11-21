package com.yugoo.gis.pojo.excel;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author nihao 2018/11/16
 */
@Data
public class ConsumerImport {
    @ExcelImport({"名称", "集团名称"})
    private String name;

    @ExcelImport("地址")
    private String position;

    @ExcelImport("建筑")
    private String buildingName;

    @ExcelImport("楼层")
    private Integer floor;

    @ExcelImport("户号")
    private String number;

    @ExcelImport("行业类别")
    private String category;

    @ExcelImport("公司性质")
    private String nature;

    @ExcelImport("公司人数")
    private Integer peopleNum;

    @ExcelImport("联系人")
    private String linkman;

    @ExcelImport("联系电话")
    private String phone;

    @ExcelImport("现有业务运营商")
    private String operator;

    @ExcelImport({"现有业务资费", "资费金额"})
    private BigDecimal expenses;

    @ExcelImport("业务到期时间")
    private String expirationDateStr;

    @ExcelImport("带宽")
    private String bandwidth;

    @ExcelImport("业务类型")
    private String serviceTypeStr;

    @ExcelImport("公司状态")
    private String status;

    @ExcelImport("法人")
    private String legal;

    @ExcelImport("专线条数")
    private Integer lineNum;

    @ExcelImport("专线类型")
    private String lineType;

    @ExcelImport("专线开户时间")
    private String lineOpenDateStr;

    @ExcelImport("专线状态")
    private String lineStatus;

    @ExcelImport("订购资费名称")
    private String expensesName;

    @ExcelImport("订购时间")
    private String orderTimeStr;

    @ExcelImport("成员角色")
    private String memberRole;

    @ExcelImport("成员真实号码")
    private String memberRoleRealNum;

    @ExcelImport("成员侧资费名称")
    private String memberExpensesName;

    @ExcelImport("集团代码")
    private String groupCode;

    @ExcelImport("集团等级")
    private String groupGrade;

    @ExcelImport("客户经理工号")
    private String userNumber;


    private Integer buildingId;
    private Long expirationDate;
    private Integer serviceType;
    private Long lineOpenDate;
    private Long orderTime;
    private Integer userId;
    private Integer type;
    private Double longitude;
    private Double latitude;

    private Integer row;

    public String getR() {
        return "第" + row + "行";
    }
}
