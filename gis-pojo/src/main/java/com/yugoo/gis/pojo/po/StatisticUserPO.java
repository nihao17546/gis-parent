package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao
 * @create 2018/11/10
 **/
@Data
public class StatisticUserPO {
    private Integer id;
    private Integer userId;
    private Integer basicArchiveCount;
    private Integer effectiveArchiveCount;
    private Integer specialLineCount;
    private Date ctime;
}
