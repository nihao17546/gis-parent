package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ConsumerInfoListVO;
import com.yugoo.gis.pojo.vo.ListVO;

import java.util.Date;

public interface IConsumerInfoService {
    ListVO<ConsumerInfoListVO> list(Integer curPage, Integer pageSize, Date startTime, Date endTime);
    void delete(Integer id);
}
