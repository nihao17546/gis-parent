package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.UserPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by nihao on 18/5/8.
 */
public interface UserDAO {
    int insert(UserPO userPO);
    UserPO selectByPhone(@Param("phone") String phone);
    UserPO selectById(@Param("id") Integer id);
    int updateRole(@Param("id") Integer id, @Param("roleId") Integer roleId);
    long selectCount(@Param("name") String name,
                     @Param("department") String department,
                     @Param("phone") String phone);
    List<UserPO> selectList(@Param("name") String name,
                            @Param("department") String department,
                            @Param("phone") String phone,
                            @Param("order") String order,
                            @Param("asc") String asc,
                            RowBounds rowBounds);
    int update(UserPO userPO);
    int updatePassword(@Param("id") Integer id,
                       @Param("newPassword") String newPassword,
                       @Param("oldPassword") String oldPassword);
}
