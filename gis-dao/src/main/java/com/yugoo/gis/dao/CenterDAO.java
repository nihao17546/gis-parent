package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.CenterPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author nihao 2018/9/20
 */
public interface CenterDAO {
    List<CenterPO> select(@Param("id") Integer id, @Param("name") String name, RowBounds rowBounds);
    CenterPO selectById(@Param("id") Integer id);
}
