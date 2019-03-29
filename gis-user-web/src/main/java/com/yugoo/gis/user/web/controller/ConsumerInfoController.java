package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.pojo.vo.ConsumerInfoListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IConsumerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/consumerInfo")
public class ConsumerInfoController extends BaseController {

    @Autowired
    private IConsumerInfoService consumerInfoService;

    @RequestMapping("/list")
    public String list(@RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) Long startTime,
                       @RequestParam(required = false) Long endTime) {
        Date start = startTime == null ? null : new Date(startTime),
                end = endTime == null ? null : new Date(endTime);
        ListVO<ConsumerInfoListVO> listVO = consumerInfoService.list(curPage, pageSize, start, end);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        consumerInfoService.delete(id);
        return ok().json();
    }

}
