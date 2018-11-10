package com.yugoo.gis.dao;

import com.yugoo.gis.pojo.po.ConfigPO;

/**
 * @author nihao 2018/11/1
 */
public interface ConfigDAO {
    int insert(ConfigPO configPO);
    int update(ConfigPO configPO);
    ConfigPO select();
    long selectCount();
    int delete();
}
