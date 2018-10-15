package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ConsumerPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author nihao 2018/10/9
 */
public interface ConsumerDAO {
    int insert(ConsumerPO consumerPO);
    ConsumerPO selectByName(@Param("name") String name);
    List<ConsumerPO> select(@Param("name") String name, @Param("roleId") Integer roleId,
                            @Param("userId") Integer userId, @Param("buildingIds") List<Integer> buildingIds,
                            RowBounds rowBounds);
    Long selectCount(@Param("name") String name, @Param("roleId") Integer roleId,
                     @Param("userId") Integer userId, @Param("buildingIds") List<Integer> buildingIds);
    ConsumerPO selectById(@Param("id") Integer id);
    ConsumerPO selectExePicById(@Param("id") Integer id);
    int update(ConsumerPO consumerPO);
    int deleteById(@Param("id") Integer id);
}
