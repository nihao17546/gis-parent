package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.utils.StringUtil;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StatisticCenterVO;
import com.yugoo.gis.pojo.vo.StatisticUserVO;
import com.yugoo.gis.user.service.IStatisticService;
import com.yugoo.gis.user.web.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.yugoo.gis.user.web.utils.ExcelUtil.statisticConsumerTitles;
import static com.yugoo.gis.user.web.utils.ExcelUtil.statisticTitles;
import static com.yugoo.gis.user.web.utils.ExcelUtil.userTitles;

/**
 * @author nihao 2018/11/5
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController extends BaseController {

    @Autowired
    private IStatisticService statisticService;

    @RequestMapping("/center")
    public String center(@RequestParam(required = false) String centerName,
                         @RequestParam(required = false, defaultValue = "1") Integer curPage,
                         @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                         @RequestParam(required = false) String sortColumn,
                         @RequestParam(required = false) String order,
                         @Value("#{request.getAttribute('uid')}") Integer uid) {
        if (!StringUtil.isEmpty(sortColumn)) {
            if (StringUtil.isEmpty(order)) {
                order = "desc";
            }
            if ("specialLineRatioStr".equalsIgnoreCase(sortColumn)) {
                sortColumn = "specialLineRatio";
            }
            else if ("hotelRatioStr".equalsIgnoreCase(sortColumn)) {
                sortColumn = "hotelRatio";
            }
            else if ("businessRatioStr".equalsIgnoreCase(sortColumn)) {
                sortColumn = "businessRatio";
            }
        }
        else {
            sortColumn = null;
            order = null;
        }
        List<StatisticCenterVO> list = statisticService.listCenter(curPage, pageSize, centerName, sortColumn, order, uid);
        return ok().pull("list", list).json();
    }

    @RequestMapping("/consumer")
    public String consumer(@RequestParam(required = false) String consumerName,
                           @RequestParam(required = false, defaultValue = "1") Integer curPage,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           @RequestParam(required = false) String order,
                           @Value("#{request.getAttribute('uid')}") Integer uid) {
        ListVO<ConsumerListVO> listVO = statisticService.listConsumer(curPage, pageSize, consumerName, order, uid);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/export/center")
    public void exportCenter(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(required = false) String centerName,
                               @RequestParam(required = false) String sortColumn,
                               @RequestParam(required = false) String order,
                             @Value("#{request.getAttribute('uid')}") Integer uid) throws NoSuchFieldException, IllegalAccessException {
        if (!StringUtil.isEmpty(sortColumn)) {
            if (StringUtil.isEmpty(order)) {
                order = "desc";
            }
            if ("specialLineRatioStr".equalsIgnoreCase(sortColumn)) {
                sortColumn = "specialLineRatio";
            }
            else if ("businessRatioStr".equalsIgnoreCase(sortColumn)) {
                sortColumn = "businessRatio";
            }
            else if ("hotelRatioStr".equalsIgnoreCase(sortColumn)) {
                sortColumn = "hotelRatio";
            }
        }
        else {
            sortColumn = null;
            order = null;
        }
        List<StatisticCenterVO> list = statisticService.listCenter(1, 5000, centerName, sortColumn, order, uid);
        Workbook workbook = ExcelUtil.prepareExport(list, statisticTitles);
        ExcelUtil.export(request, response, workbook, "营销中心业务统计");
    }

    @RequestMapping("/export/consumer")
    public void exportConsumer(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(required = false) String consumerName,
                               @RequestParam(required = false) String order,
                               @Value("#{request.getAttribute('uid')}") Integer uid) throws NoSuchFieldException, IllegalAccessException {
        ListVO<ConsumerListVO> listVO = statisticService.listConsumer(1, 5000, consumerName, order, uid);
        Workbook workbook = ExcelUtil.prepareExport(listVO.getList(), statisticConsumerTitles);
        ExcelUtil.export(request, response, workbook, "即将到期业务汇总");
    }

    @RequestMapping("/user")
    public String user(@RequestParam(required = false) String centerName,
                       @RequestParam(required = false) String sortColumn,
                       @RequestParam(required = false) String order,
                       @Value("#{request.getAttribute('uid')}") Integer uid) {
        List<StatisticUserVO> list = statisticService.listUser(centerName, sortColumn, order, uid);
        return ok().pull("list", list).json();
    }

    @RequestMapping("/export/user")
    public void exportUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(required = false) String centerName,
                           @RequestParam(required = false) String sortColumn,
                           @RequestParam(required = false) String order,
                           @Value("#{request.getAttribute('uid')}") Integer uid) throws NoSuchFieldException, IllegalAccessException {
        List<StatisticUserVO> list = statisticService.listUser(centerName, sortColumn, order, uid);
        Workbook workbook = ExcelUtil.prepareExport(list, userTitles);
        ExcelUtil.export(request, response, workbook, "客户经理业务统计");
    }
}
