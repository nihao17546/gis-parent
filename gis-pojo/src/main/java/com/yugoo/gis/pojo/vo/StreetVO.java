package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.StreetPO;
import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/9/30
 */
@Data
public class StreetVO extends StreetPO {
    private String typeName;
    private List<CompetitorVO> competitors;
    private List<PointVO> buildingPoints;
}
