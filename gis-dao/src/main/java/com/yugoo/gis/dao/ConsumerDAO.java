package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ConsumerPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author nihao 2018/10/9
 */
public interface ConsumerDAO {
    int insert(ConsumerPO consumerPO);
    ConsumerPO selectByName(@Param("name") String name);
}
