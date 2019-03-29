package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ConsumerInfoPO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ConsumerInfoDAO {
    Long selectCount(@Param("startTime") Date startTime,
                     @Param("endTime") Date endTime);

    List<ConsumerInfoPO> select(@Param("startTime") Date startTime,
                                @Param("endTime") Date endTime,
                                @Param("offset") Integer offset,
                                @Param("rows") Integer rows);

    int deleteById(@Param("id") Integer id);
}
