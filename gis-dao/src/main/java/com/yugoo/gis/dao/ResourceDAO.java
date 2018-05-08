package com.yugoo.gis.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by nihao on 18/5/8.
 */
public interface ResourceDAO {
    List<String> selectPathByRoleId(@Param("roleId") Integer roleId);
}
