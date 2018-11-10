package com.yugoo.gis.pojo.vo;

import com.yugoo.gis.pojo.po.UserPO;
import lombok.Data;

/**
 * @author nihao 2018/9/20
 */
@Data
public class UserListVO extends UserPO {
     private String roleName;
    private String groupName;
    private String centerName;

}
