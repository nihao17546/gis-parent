package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ResourcePO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author nihao 2018/10/23
 */
public interface ResourceDAO {
    int insert(ResourcePO resourcePO);
    int update(ResourcePO resourcePO);
    List<ResourcePO> select(@Param("buildingId") Integer buildingId,
                            RowBounds rowBounds);
    Long selectCount(@Param("buildingId") Integer buildingId);
    ResourcePO selectByBuildingIdAndFloorAndNumber(@Param("buildingId") Integer buildingId,
                                                   @Param("floor") Integer floor,
                                                   @Param("number") String number,
                                                   @Param("longitude") Double longitude,
                                                   @Param("latitude") Double latitude);
    int delete(@Param("id") Integer id);
    Long selectCountByBuildingId(@Param("buildingId") Integer buildingId);
    int updateLoAndLaByBuildingId(@Param("buildingId") Integer buildingId,
                                  @Param("longitude") Double longitude,
                                  @Param("latitude") Double latitude);
    int batchInsert(@Param("list") List<ResourcePO> list);
    List<ResourcePO> selectByCenter(@Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                    @Param("laMin") Double laMin, @Param("laMax") Double laMax);
    List<ResourcePO> selectByBuildingIds(@Param("buildingIds") List<Integer> buildingIds);
    ResourcePO selectById(@Param("id") Integer id);
}
