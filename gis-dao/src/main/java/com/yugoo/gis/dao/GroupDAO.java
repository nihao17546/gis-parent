package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.GroupPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author nihao 2018/9/20
 */
public interface GroupDAO {
    int insert(GroupPO groupPO);
    List<GroupPO> select(@Param("name") String name, RowBounds rowBounds);
    Long selectCount(@Param("name") String name);
    int update(GroupPO groupPO);
    GroupPO selectById(@Param("id") Integer id);
    int deleteById(@Param("id") Integer id);
    @MapKey("id")
    Map<Integer,GroupPO> selectByIds(@Param("ids") List<Integer> ids);
    GroupPO selectByName(@Param("name") String name);
}
