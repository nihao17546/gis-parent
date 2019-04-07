package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.utils.ZipUtils;
import com.yugoo.gis.pojo.vo.ConsumerInfoListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IConsumerInfoService;
import com.yugoo.gis.user.web.utils.ExcelUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.yugoo.gis.user.web.utils.ExcelUtil.customerInfoTitles;

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

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(required = false) Long startTime,
                       @RequestParam(required = false) Long endTime) throws NoSuchFieldException, IllegalAccessException {
        Date start = startTime == null ? null : new Date(startTime),
                end = endTime == null ? null : new Date(endTime);
        ListVO<ConsumerInfoListVO> listVO = consumerInfoService.list(1, 5000, start, end);
        Workbook workbook = ExcelUtil.prepareExport(listVO.getList(), customerInfoTitles);
        ExcelUtil.export(request, response, workbook, "扫街扫铺");
    }

    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam String param) throws IOException {
        String[] str = param.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String s : str) {
            ids.add(Integer.parseInt(s));
        }
        List<ZipUtils.BufferFile> bufferFileList = consumerInfoService.getBufferFiles(ids);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=photo"
                + UUID.randomUUID().toString().substring(0,8)+".zip");
        ServletOutputStream outputStream = response.getOutputStream();
        ZipUtils.toZip(outputStream, bufferFileList);
        outputStream.close();
    }

}
