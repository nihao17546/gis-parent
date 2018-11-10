package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.ConsumerPO;
import lombok.Data;

/**
 * @author nihao
 * @create 2018/10/11
 **/
@Data
public class ConsumerListVO extends ConsumerPO {
    private String typeName;
    private String buildingName;
    private String serviceTypeName;
    private String expirationDateStr;
    private String lineOpenDateStr;
    private String userName;
    private String orderTimeStr;
}
