package com.yugoo.gis.user.service;

import com.yugoo.gis.common.utils.ZipUtils;
import com.yugoo.gis.pojo.vo.ConsumerInfoListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StatisticCustomerVO;

import java.util.Date;
import java.util.List;

public interface IConsumerInfoService {
    ListVO<ConsumerInfoListVO> list(Integer curPage, Integer pageSize, Date startTime, Date endTime);
    void delete(Integer id);
    List<StatisticCustomerVO> statistic(Date startTime, Date endTime);
    List<ZipUtils.BufferFile> getBufferFiles(List<Integer> ids);
}
