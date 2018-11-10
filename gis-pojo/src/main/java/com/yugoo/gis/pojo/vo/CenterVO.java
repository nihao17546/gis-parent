package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.CenterPO;
import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/9/28
 */
@Data
public class CenterVO extends CenterPO {
    private String groupName;
    private List<PointVO> points;
}
