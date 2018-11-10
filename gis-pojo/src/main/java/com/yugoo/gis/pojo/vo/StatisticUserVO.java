package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.StatisticUserPO;
import lombok.Data;

/**
 * @author nihao
 * @create 2018/11/9
 **/
@Data
public class StatisticUserVO extends StatisticUserPO {
    private String userName;
    private Integer centerId;
    private String centerName;
}
