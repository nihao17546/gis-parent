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
    int update(UserPO userPO);
    List<UserPO> select(@Param("name") String name, @Param("id") Integer id,
                        @Param("phone") String phone, RowBounds rowBounds);
    Long selectCount(@Param("name") String name, @Param("id") Integer id,
                     @Param("phone") String phone);
    UserPO selectByPhone(@Param("phone") String phone);
    UserPO selectById(@Param("id") Integer id);
    int deleteById(@Param("id") Integer id);
    UserPO selectManager(@Param("groupId") Integer groupId);
    List<UserPO> selectByGroupId(@Param("groupId") Integer groupId);
    List<UserPO> selectByCenterId(@Param("centerId") Integer centerId);
}
