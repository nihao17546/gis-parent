package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.StatisticCenterPO;
import lombok.Data;

/**
 * @author nihao 2018/11/5
 */
@Data
public class StatisticCenterVO extends StatisticCenterPO {
    private String centerName;
    private String district;
    private String specialLineRatioStr;
    private String hotelRatioStr;
    private String businessRatioStr;
}
