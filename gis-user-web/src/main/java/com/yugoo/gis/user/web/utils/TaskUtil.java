package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.user.service.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author nihao 2018/11/5
 */
public class TaskUtil {

    @Autowired
    private IStatisticService statisticService;

    public void center() {
        statisticService.statisticCenter();
        statisticService.statisticUser();
    }

}
