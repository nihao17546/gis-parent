package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.RolePO;

import java.util.List;


/**
 * Created by nihao on 18/5/10.
 */
public interface RoleDAO {
    List<RolePO> selectAll();
}
