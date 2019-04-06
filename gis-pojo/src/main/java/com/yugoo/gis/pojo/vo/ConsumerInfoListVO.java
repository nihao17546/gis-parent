package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.ConsumerInfoPO;
import lombok.Data;

import java.util.Date;

@Data
public class ConsumerInfoListVO extends ConsumerInfoPO {
    private String groupName;
    private String userName;
    private String statusStr;
    private Date transactedTimeDate;
    private Date bookedTimeDate;
}
