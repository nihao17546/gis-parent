package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.GroupPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author nihao 2018/9/20
 */
public interface GroupDAO {
    int insert(GroupPO groupPO);
    List<GroupPO> select(@Param("name") String name, RowBounds rowBounds);
    int update(GroupPO groupPO);
}
