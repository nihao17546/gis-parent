package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.CenterPO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author nihao
 * @create 2018/10/7
 **/
@Data
public class GroupVO {
    private Integer id;
    private String name;
    private String position;
    private Date ctime;

    private String managerName;
    private String managerPhone;
    private List<CenterPO> centers;
}
