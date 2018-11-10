package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.GroupPO;
import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/9/27
 */
@Data
public class GroupListVO extends GroupPO {
    private String managerName;
    private String managerPhone;
    private List<PointVO> centerPoints;
}
