package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.LearnPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author nihao 2019/3/22
 */
public interface LearnDAO {
    int insert(LearnPO learnPO);
    int update(LearnPO learnPO);
    LearnPO selectById(@Param("id") Integer id);
    Long selectCount(@Param("title") String title);
    List<LearnPO> select(@Param("title") String title, RowBounds rowBounds);
    int deleteById(@Param("id") Integer id);
}
