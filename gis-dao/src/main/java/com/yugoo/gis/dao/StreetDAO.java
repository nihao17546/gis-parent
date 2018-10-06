package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.StreetPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author nihao 2018/9/30
 */
public interface StreetDAO {
    int insert(StreetPO streetPO);
    StreetPO selectByName(@Param("name") String name);
    List<StreetPO> select(@Param("name") String name, RowBounds rowBounds);
    Long selectCount(@Param("name") String name);
    int update(StreetPO streetPO);
    StreetPO selectById(@Param("id") Integer id);
    @MapKey("id")
    Map<Integer,StreetPO> selectByIds(@Param("ids") List<Integer> ids);
    int updateLoAndLa(@Param("id") Integer id, @Param("longitude") Double longitude,
                      @Param("latitude") Double latitude);
}
