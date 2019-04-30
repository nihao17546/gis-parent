package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.BuildingPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author nihao
 * @create 2018/10/5
 **/
public interface BuildingDAO {
    int insert(BuildingPO buildingPO);
    BuildingPO selectByName(@Param("name") String name);
    List<BuildingPO> select(@Param("name") String name, @Param("streetId") Integer streetId,
                            @Param("offset") Integer offset,
                            @Param("rows") Integer rows);
    Long selectCount(@Param("name") String name, @Param("streetId") Integer streetId);
    int update(BuildingPO buildingPO);
    BuildingPO selectAvgByStreetId(@Param("streetId") Integer streetId);
    BuildingPO selectById(@Param("id") Integer id);
    List<BuildingPO> selectAllByLoAndLa(@Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                        @Param("laMin") Double laMin, @Param("laMax") Double laMax);
    List<BuildingPO> selectByLoAndLa(@Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                     @Param("laMin") Double laMin, @Param("laMax") Double laMax,
                                     @Param("offset") Integer offset, @Param("rows") Integer rows);
    Long selectCountByLoAndLa(@Param("loMin") Double loMin, @Param("loMax") Double loMax,
                              @Param("laMin") Double laMin, @Param("laMax") Double laMax);
    @MapKey("id")
    Map<Integer,BuildingPO> selectByIds(@Param("ids") List<Integer> ids);
    List<BuildingPO> selectByStreetIds(@Param("streetIds") List<Integer> streetIds);
    List<BuildingPO> selectByStreetId(@Param("streetId") Integer streetId);
    List<BuildingPO> selectByLoAndLaAndName(@Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                            @Param("laMin") Double laMin, @Param("laMax") Double laMax,
                                            @Param("name") String name, @Param("limit") Integer limit);
    int deleteById(@Param("id") Integer id);
    List<BuildingPO> selectLikeName(@Param("name") String name);
    List<BuildingPO> selectFromMap(@Param("name") String name,
                                   @Param("loMin") Double loMin, @Param("loMax") Double loMax,
                                   @Param("laMin") Double laMin, @Param("laMax") Double laMax, @Param("limit") Integer limit);
}
