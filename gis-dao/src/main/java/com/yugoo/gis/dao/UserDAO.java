package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.UserPO;
import org.apache.ibatis.annotations.Param;

/**
 * Created by nihao on 18/5/8.
 */
public interface UserDAO {
    int insert(UserPO userPO);
    UserPO selectByPhone(@Param("phone") String phone);
    UserPO selectById(@Param("id") Integer id);
    int updateRole(@Param("id") Integer id, @Param("roleId") Integer roleId);
}
