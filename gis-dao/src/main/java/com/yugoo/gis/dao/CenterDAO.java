package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.CenterPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author nihao 2018/9/20
 */
public interface CenterDAO {
    List<CenterPO> select(@Param("name") String name, RowBounds rowBounds);
    Long selectCount(@Param("name") String name);
    CenterPO selectById(@Param("id") Integer id);
    List<CenterPO> selectAll(@Param("groupId") Integer groupId);
    List<CenterPO> selectByGroupId(@Param("groupId") Integer groupId);
    @MapKey("id")
    Map<Integer,CenterPO> selectByIds(@Param("ids") List<Integer> ids);
    int deleteById(@Param("id") Integer id);
    CenterPO selectByName(@Param("name") String name);
    int insert(CenterPO centerPO);
    int update(CenterPO centerPO);
}
