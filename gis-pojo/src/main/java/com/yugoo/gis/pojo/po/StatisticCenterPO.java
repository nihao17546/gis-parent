package com.yugoo.gis.pojo.po;

import lombok.Data;

import java.util.Date;

/**
 * @author nihao 2018/11/5
 */
@Data
public class StatisticCenterPO {
    private Integer id;
    private Integer centerId;
    private Integer notArchiveCount;
    private Integer basicArchiveCount;
    private Integer effectiveArchiveCount;
    private Integer wholePortCount;
    private Integer usedPortCount;
    private Float specialLineRatio;
    private Float hotelRatio;
    private Float businessRatio;
    private Date ctime;
}
