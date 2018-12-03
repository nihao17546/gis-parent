package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.ResourcePO;
import lombok.Data;


/**
 * @author nihao 2018/10/23
 */
@Data
public class ResourceVO extends ResourcePO {
    private String buildingName;
    private String streetName;
}
