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
    List<CenterPO> select(@Param("name") String name, @Param("groupId") Integer groupId, RowBounds rowBounds);
    Long selectCount(@Param("name") String name, @Param("groupId") Integer groupId);
    CenterPO selectById(@Param("id") Integer id);
    List<CenterPO> selectAll(@Param("groupId") Integer groupId);
    List<CenterPO> selectByGroupId(@Param("groupId") Integer groupId);
    @MapKey("id")
    Map<Integer,CenterPO> selectByIds(@Param("ids") List<Integer> ids);
    int deleteById(@Param("id") Integer id);
    CenterPO selectByName(@Param("name") String name);
    int insert(CenterPO centerPO);
    int update(CenterPO centerPO);
    List<CenterPO> selectLikeName(@Param("name") String name);
    List<CenterPO> selectFromMap(@Param("name") String name,
                                 @Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                 @Param("laMin") Double laMin, @Param("laMax") Double laMax,
                                 @Param("groupId") Integer groupId);
    List<Integer> selectIdByGroupIdAndLikeName(@Param("id") Integer id,
                                               @Param("groupId") Integer groupId,
                                                     @Param("name") String name);
}
