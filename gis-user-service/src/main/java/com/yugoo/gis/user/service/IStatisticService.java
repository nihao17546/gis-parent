package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StatisticCenterVO;
import com.yugoo.gis.pojo.vo.StatisticUserVO;

import java.util.List;

/**
 * @author nihao 2018/11/5
 */
public interface IStatisticService {
    List<StatisticCenterVO> listCenter(Integer curPage, Integer pageSize, String centerName, String sortColumn, String order, Integer currentUserId);
    void statisticCenter();
    void statisticUser();
    ListVO<ConsumerListVO> listConsumer(Integer curPage, Integer pageSize, String consumerName, String order, Integer currentUserId);
    List<StatisticUserVO> listUser(String centerName, String sortColumn, String order, Integer currentUserId);
}
