package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.ConfigDAO;
import com.yugoo.gis.pojo.po.ConfigPO;
import com.yugoo.gis.pojo.vo.ConfigVO;
import com.yugoo.gis.user.service.IConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author nihao 2018/11/1
 */
@Service
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private ConfigDAO configDAO;

    @Transactional
    @Override
    public void createOrEdit(Integer mapSearchRegion, Integer expirationDateLimit) {
        configDAO.delete();
        ConfigPO configPO = new ConfigPO();
        configPO.setExpirationDateLimit(expirationDateLimit);
        configPO.setMapSearchRegion(mapSearchRegion);
        configDAO.insert(configPO);
    }

    @Transactional
    @Override
    public ConfigVO get() {
        ConfigPO configPO = configDAO.select();
        if (configPO == null) {
            configPO = new ConfigPO();
            configPO.setMapSearchRegion(1000);
            configPO.setExpirationDateLimit(1);
            configDAO.insert(configPO);
        }
        ConfigVO vo = new ConfigVO();
        BeanUtils.copyProperties(configPO, vo);
        return vo;
    }
}
