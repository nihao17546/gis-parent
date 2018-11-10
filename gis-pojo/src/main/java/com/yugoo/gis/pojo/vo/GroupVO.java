package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.GroupPO;
import lombok.Data;

import java.util.List;

/**
 * @author nihao
 * @create 2018/10/7
 **/
@Data
public class GroupVO extends GroupPO {
    private String managerName;
    private String managerPhone;
    private List<CenterPO> centers;
}
