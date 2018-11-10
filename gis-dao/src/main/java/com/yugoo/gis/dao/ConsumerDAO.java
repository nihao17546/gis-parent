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
    List<ConsumerPO> select(@Param("name") String name,
                            @Param("userIds") List<Integer> userIds,
                            @Param("buildingIds") List<Integer> buildingIds,
                            RowBounds rowBounds);
    Long selectCount(@Param("name") String name,
                     @Param("userIds") List<Integer> userIds,
                     @Param("buildingIds") List<Integer> buildingIds);
    ConsumerPO selectById(@Param("id") Integer id);
    ConsumerPO selectExePicById(@Param("id") Integer id);
    int update(ConsumerPO consumerPO);
    int deleteById(@Param("id") Integer id);
    Long selectCountByBuildingId(@Param("buildingId") Integer buildingId);
    int updateLoAndLaByBuildingId(@Param("buildingId") Integer buildingId,
                                  @Param("longitude") Double longitude,
                                  @Param("latitude") Double latitude);
    List<ConsumerPO> selectFromMap(@Param("name") String name,
                                   @Param("userIds") List<Integer> userIds,
                                   @Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                   @Param("laMin") Double laMin, @Param("laMax") Double laMax,
                                   @Param("buildingIds") List<Integer> buildingIds);
    List<ConsumerPO> selectByCenter(@Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                    @Param("laMin") Double laMin, @Param("laMax") Double laMax);
}
