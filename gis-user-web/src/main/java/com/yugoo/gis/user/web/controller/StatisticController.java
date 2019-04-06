package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.utils.SimpleDateUtil;
import com.yugoo.gis.common.utils.StringUtil;
import com.yugoo.gis.pojo.vo.*;
import com.yugoo.gis.user.service.IConsumerInfoService;
import com.yugoo.gis.user.service.IStatisticService;
import com.yugoo.gis.user.web.utils.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.yugoo.gis.user.web.utils.ExcelUtil.*;

/**
 * @author nihao 2018/11/5
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController extends BaseController {

    @Autowired
    private IStatisticService statisticService;

    @Autowired
    private IConsumerInfoService consumerInfoService;

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

    @RequestMapping("/customerInfo")
    public String list(@RequestParam(required = false) String startTime,
                       @RequestParam(required = false) String endTime) {
        Date start = startTime == null ? null : SimpleDateUtil.parse(startTime + " 00:00:00"),
                end = endTime == null ? null : SimpleDateUtil.parse(endTime + " 23:59:59");
        if (start == null || end == null) {
            start = SimpleDateUtil.initDateByMonth();
            end = new Date();
        }
        List<StatisticCustomerVO> list = consumerInfoService.statistic(start, end);
        return ok().pull("list", list).pull("start", SimpleDateUtil.shortFormat(start))
                .pull("end", SimpleDateUtil.shortFormat(end)).json();
    }

    @RequestMapping("/export/customerInfo")
    public void exportUser(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(required = false) String startTime,
                           @RequestParam(required = false) String endTime) throws NoSuchFieldException, IllegalAccessException {
        Date start = startTime == null ? null : SimpleDateUtil.parse(startTime + " 00:00:00"),
                end = endTime == null ? null : SimpleDateUtil.parse(endTime + " 23:59:59");
        if (start == null || end == null) {
            start = SimpleDateUtil.initDateByMonth();
            end = new Date();
        }
        List<StatisticCustomerVO> list = consumerInfoService.statistic(start, end);
        Workbook workbook = ExcelUtil.prepareExport(list, statisticCustomerInfoTitles);
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        Font fontA = workbook.createFont();
        fontA.setFontHeight((short)(22*12));//字体大小
        fontA.setFontName("宋体");//字体
        fontA.setItalic(false);//是否倾斜
        titleStyle.setFont(fontA);
        titleStyle.setWrapText(true);//是否换行
        titleStyle.setBorderBottom(BorderStyle.THIN);
        titleStyle.setBorderLeft(BorderStyle.THIN);
        titleStyle.setBorderRight(BorderStyle.THIN);
        titleStyle.setBorderTop(BorderStyle.THIN);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNo = sheet.getLastRowNum();
        sheet.shiftRows(0, lastRowNo, 1);
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("查询日期范围");
        row.createCell(1).setCellValue(SimpleDateUtil.cnFormat(start)
                + "至" + SimpleDateUtil.cnFormat(end));
        row.createCell(2).setCellValue("统计表生成时间");
        row.createCell(3).setCellValue(SimpleDateUtil.format(new Date()));
        row.getCell(0).setCellStyle(titleStyle);
        row.getCell(1).setCellStyle(titleStyle);
        row.getCell(2).setCellStyle(titleStyle);
        row.getCell(3).setCellStyle(titleStyle);
        ExcelUtil.export(request, response, workbook, "扫街扫铺统计");
    }

}
