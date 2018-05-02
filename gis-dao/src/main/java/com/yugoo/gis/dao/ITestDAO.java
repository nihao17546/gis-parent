package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.TestPO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * Created by nihao on 18/2/27.
 */
public interface ITestDAO {
    TestPO select();
    int update(@Param("name") String name);
    int uu(@Param("aa")BigDecimal aa);
}
