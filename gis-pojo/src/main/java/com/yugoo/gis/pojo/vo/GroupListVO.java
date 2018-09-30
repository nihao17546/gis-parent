package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author nihao 2018/9/27
 */
@Data
public class GroupListVO {
    private Integer id;
    private String name;
    private String position;
    private Date ctime;

    private String managerName;
    private String managerPhone;
    private List<PointVO> centerPoints;
}
