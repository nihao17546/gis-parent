package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.ConsumerDAO;
import com.yugoo.gis.pojo.po.ConsumerPO;
import com.yugoo.gis.user.service.IConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author nihao 2018/10/9
 */
@Service
public class ConsumerServiceImpl implements IConsumerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConsumerDAO consumerDAO;

    @Override
    public void create(String name, Integer buildingId, String floor, String position, String number,
                       String category, String nature, Integer peopleNum, byte[] pic, String status, String legal) {
        ConsumerPO check = consumerDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        ConsumerPO consumerPO = new ConsumerPO();
        consumerPO.setName(name);
        consumerPO.setBuildingId(buildingId);
        consumerPO.setFloor(floor);
        consumerPO.setPosition(position);
        consumerPO.setNumber(number);
        consumerPO.setCategory(category);
        consumerPO.setNature(nature);
        consumerPO.setPeopleNum(peopleNum);
        consumerPO.setPic(pic);
        consumerPO.setStatus(status);
        consumerPO.setLegal(legal);
        consumerDAO.insert(consumerPO);
    }
}
