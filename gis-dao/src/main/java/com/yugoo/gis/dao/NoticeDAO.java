package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.NoticePO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author nihao 2019/3/22
 */
public interface NoticeDAO {
    int insert(NoticePO noticePO);
    int update(NoticePO noticePO);
    NoticePO selectById(@Param("id") Integer id);
    Long selectCount(@Param("title") String title);
    List<NoticePO> select(@Param("title") String title, RowBounds rowBounds);
    int deleteById(@Param("id") Integer id);
}
