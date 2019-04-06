package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ConsumerInfoPO;
import com.yugoo.gis.pojo.po.StatisticCustomerPO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ConsumerInfoDAO {
    Long selectCount(@Param("startTime") Date startTime,
                     @Param("endTime") Date endTime);

    List<ConsumerInfoPO> select(@Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("offset") Integer offset,
                                @Param("rows") Integer rows);

    int deleteById(@Param("id") Integer id);

    @MapKey("userId")
    Map<Integer,StatisticCustomerPO> selectByCtime(@Param("start") Date start,
                                           @Param("end") Date end);

    @MapKey("userId")
    Map<Integer,StatisticCustomerPO>  selectByBookedTime(@Param("start") Long start,
                                                 @Param("end") Long end);

    @MapKey("userId")
    Map<Integer,StatisticCustomerPO>  selectByTransactedTime(@Param("start") Long start,
                                                     @Param("end") Long end);
}
