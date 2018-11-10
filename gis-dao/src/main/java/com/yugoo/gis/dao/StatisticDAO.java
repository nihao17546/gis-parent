package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ConsumerPO;
import com.yugoo.gis.pojo.po.StatisticCenterPO;
import com.yugoo.gis.pojo.po.StatisticUserPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author nihao 2018/11/5
 */
public interface StatisticDAO {
    int batchInsertCenter(@Param("list") List<StatisticCenterPO> list);
    int deleteCenter();
    List<StatisticCenterPO> selectCenter(@Param("centerIds") List<Integer> centerIds,
                                   @Param("sortColumn") String sortColumn,
                                   @Param("order") String order);
    List<ConsumerPO> selectConsumer(@Param("expirationDate") Long expirationDate,
                                    @Param("userIds") List<Integer> userIds,
                                    @Param("name") String name,
                                    @Param("order") String order,
                                    RowBounds rowBounds);
    Long selectConsumerCount(@Param("expirationDate") Long expirationDate,
                             @Param("userIds") List<Integer> userIds,
                             @Param("name") String name,
                             @Param("order") String order);
    @MapKey("userId")
    Map<Integer,StatisticUserPO> selectStatisticUser(@Param("userIds") List<Integer> userIds);
    int batchInsertUser(@Param("list") List<StatisticUserPO> list);
    int deleteUser();
    List<StatisticUserPO> selectUser(@Param("userIds") List<Integer> userIds,
                                     @Param("sortColumn") String sortColumn,
                                     @Param("order") String order);
}
