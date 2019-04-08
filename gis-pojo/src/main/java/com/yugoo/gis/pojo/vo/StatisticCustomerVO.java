package com.yugoo.gis.pojo.vo;

import lombok.Data;

/**
 * @author nihao 2019/4/6
 */
@Data
public class StatisticCustomerVO {
    private String userName;
    private String groupName;
    private Integer userId;
    private Integer todayTotalCount;
    private Integer todayBookedCount;
    private Integer todayTransactedCount;
    private Integer accumulateBookedCount;
    private Integer accumulateTransactedCount;

    public StatisticCustomerVO() {
        todayTotalCount = 0;
        todayBookedCount = 0;
        todayTransactedCount = 0;
        accumulateBookedCount = 0;
        accumulateTransactedCount = 0;
    }
}
