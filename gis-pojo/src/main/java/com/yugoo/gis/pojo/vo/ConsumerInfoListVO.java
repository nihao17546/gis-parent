package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.ConsumerInfoPO;
import lombok.Data;

@Data
public class ConsumerInfoListVO extends ConsumerInfoPO {
    private String groupName;
    private String userName;
}
