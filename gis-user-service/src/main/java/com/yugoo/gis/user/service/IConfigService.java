package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ConfigVO;

/**
 * @author nihao 2018/11/1
 */
public interface IConfigService {
    void createOrEdit(Integer mapSearchRegion, Integer expirationDateLimit);
    ConfigVO get();
}
