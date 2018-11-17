package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.constant.ServiceType;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.SimpleDateUtil;
import com.yugoo.gis.pojo.excel.ConsumerImport;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ConsumerVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StreetTypeVO;
import com.yugoo.gis.user.service.IConsumerService;
import com.yugoo.gis.user.web.utils.ExcelUtil;
import com.yugoo.gis.user.web.utils.ReadDataUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.yugoo.gis.user.web.utils.ExcelUtil.consumerTitles;

/**
 * @author nihao 2018/10/9
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController extends BaseController {

    @Autowired
    private IConsumerService consumerService;

    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam(required = false) Integer buildingId,
                         @RequestParam(required = false) String floor, @RequestParam(required = false) String number,
                         @RequestParam(required = false) String position, @RequestParam(required = false) String category,
                         @RequestParam(required = false) String nature, @RequestParam(required = false) Integer peopleNum,
                         @RequestParam(required = false) String linkman, @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String operator, @RequestParam(required = false) String expenses,
                         @RequestParam(required = false) String expirationDateStr, @RequestParam(required = false) String bandwidth,
                         @RequestParam(required = false) Integer serviceType, @RequestParam(required = false) String status,
                         @RequestParam(required = false) String legal, @RequestParam(required = false) Integer lineNum,
                         @RequestParam(required = false) String lineType, @RequestParam(required = false) String lineOpenDateStr,
                         @RequestParam(required = false) String lineStatus, @RequestParam(required = false) String groupCode,
                         @RequestParam(required = false) String groupGrade,
                         @RequestParam(required = false) Integer bindUserId,
                         @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @Value("#{request.getAttribute('role')}") Integer role,
                         @Value("#{request.getAttribute('uid')}") Integer uid,
                         @RequestParam(required = false) Double longitude,
                         @RequestParam(required = false) Double latitude,
                         @RequestParam(required = false) String expensesName,
                         @RequestParam(required = false) String orderTimeStr,
                         @RequestParam(required = false) String memberRole,
                         @RequestParam(required = false) String memberRoleRealNum,
                         @RequestParam(required = false) String memberExpensesName
                         ) throws IOException {
        byte[] pic = null;
        if (multipartFile != null) {
            pic = multipartFile.getBytes();
        }
        Long expirationDate = 0L;
        if (expirationDateStr != null) {
            expirationDate = SimpleDateUtil.shortParse(expirationDateStr).getTime();
        }
        Long lineOpenDate = 0L;
        if (lineOpenDateStr != null) {
            lineOpenDate = SimpleDateUtil.shortParse(lineOpenDateStr).getTime();
        }
        Long orderTime = 0L;
        if (orderTimeStr != null) {
            orderTime = SimpleDateUtil.shortParse(orderTimeStr).getTime();
        }
        try {
            consumerService.create(name, buildingId, floor, number, position, pic, category,
                    nature, peopleNum, linkman, phone, operator, expenses == null ? null : new BigDecimal(expenses),
                    expirationDate, bandwidth, serviceType, status, legal, lineNum, lineType,
                    lineOpenDate, lineStatus, groupCode, groupGrade, new UserPO(uid, role), bindUserId, longitude, latitude,
                    expensesName, orderTime, memberRole, memberRoleRealNum, memberExpensesName);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam Integer id, @RequestParam String name, @RequestParam Integer buildingId,
                       @RequestParam(required = false) String floor, @RequestParam(required = false) String number,
                       @RequestParam(required = false) String position, @RequestParam(required = false) String category,
                       @RequestParam(required = false) String nature, @RequestParam(required = false) Integer peopleNum,
                       @RequestParam(required = false) String linkman, @RequestParam(required = false) String phone,
                       @RequestParam(required = false) String operator, @RequestParam(required = false) String expenses,
                       @RequestParam(required = false) String expirationDateStr, @RequestParam(required = false) String bandwidth,
                       @RequestParam(required = false) Integer serviceType, @RequestParam(required = false) String status,
                       @RequestParam(required = false) String legal, @RequestParam(required = false) Integer lineNum,
                       @RequestParam(required = false) String lineType, @RequestParam(required = false) String lineOpenDateStr,
                       @RequestParam(required = false) String lineStatus, @RequestParam(required = false) String groupCode,
                       @RequestParam(required = false) String groupGrade,
                       @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                       @RequestParam(required = false) Integer bindUserId,
                       @Value("#{request.getAttribute('uid')}") Integer uid,
                       @Value("#{request.getAttribute('role')}") Integer role,
                       @RequestParam(required = false) String expensesName,
                       @RequestParam(required = false) String orderTimeStr,
                       @RequestParam(required = false) String memberRole,
                       @RequestParam(required = false) String memberRoleRealNum,
                       @RequestParam(required = false) String memberExpensesName) throws IOException {
        byte[] pic = null;
        if (multipartFile != null) {
            pic = multipartFile.getBytes();
        }
        Long expirationDate = 0L;
        if (expirationDateStr != null) {
            expirationDate = SimpleDateUtil.shortParse(expirationDateStr).getTime();
        }
        Long lineOpenDate = 0L;
        if (lineOpenDateStr != null) {
            lineOpenDate = SimpleDateUtil.shortParse(lineOpenDateStr).getTime();
        }
        Long orderTime = 0L;
        if (orderTimeStr != null) {
            orderTime = SimpleDateUtil.shortParse(orderTimeStr).getTime();
        }
        try {
            consumerService.update(name, buildingId, floor, number, position, pic, category,
                    nature, peopleNum, linkman, phone, operator, expenses == null ? null : new BigDecimal(expenses),
                    expirationDate, bandwidth, serviceType, status, legal, lineNum, lineType,
                    lineOpenDate, lineStatus, groupCode, groupGrade, new UserPO(uid, role), bindUserId, id,
                    expensesName, orderTime, memberRole, memberRoleRealNum, memberExpensesName);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String name,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @Value("#{request.getAttribute('uid')}") Integer uid,
                       @Value("#{request.getAttribute('role')}") Integer role,
                       @RequestParam(required = false) Integer buildingId,
                       @RequestParam(required = false) Integer id) {
        ListVO<ConsumerListVO> listVO = null;
        if (id == null || id == 0) {
            listVO = consumerService.list(curPage, pageSize, name, new UserPO(uid, role), buildingId);
        }
        // 从地图首页跳转过来的
        else {
            ConsumerVO consumerVO = consumerService.getById(id);
            ConsumerListVO vo = new ConsumerListVO();
            BeanUtils.copyProperties(consumerVO, vo);
            listVO = new ListVO<>();
            listVO.setCurPage(1);
            listVO.setPageSize(10);
            listVO.setTotalPage(1);
            listVO.setTotalCount(1);
            List<ConsumerListVO> list = new ArrayList<>();
            list.add(vo);
            listVO.setList(list);
        }
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(required = false) String name,
                       @Value("#{request.getAttribute('uid')}") Integer uid,
                       @Value("#{request.getAttribute('role')}") Integer role,
                       @RequestParam(required = false) Integer buildingId,
                       @RequestParam(required = false) Integer id) throws NoSuchFieldException, IllegalAccessException {
        ListVO<ConsumerListVO> listVO = null;
        if (id == null || id == 0) {
            listVO = consumerService.list(1, 5000, name, new UserPO(uid, role), buildingId);
        }
        else {
            ConsumerVO consumerVO = consumerService.getById(id);
            ConsumerListVO vo = new ConsumerListVO();
            BeanUtils.copyProperties(consumerVO, vo);
            listVO = new ListVO<>();
            listVO.setCurPage(1);
            listVO.setPageSize(10);
            listVO.setTotalPage(1);
            listVO.setTotalCount(1);
            List<ConsumerListVO> list = new ArrayList<>();
            list.add(vo);
            listVO.setList(list);
        }
        Workbook workbook = ExcelUtil.prepareExport(listVO.getList(), consumerTitles);
        ExcelUtil.export(request, response, workbook, "客户信息");
    }

    @RequestMapping("/serviceTypes")
    public String types() {
        List<StreetTypeVO> list = new ArrayList<>();
        for (ServiceType serviceType : ServiceType.values()) {
            StreetTypeVO streetTypeVO = new StreetTypeVO();
            streetTypeVO.setId(serviceType.getValue());
            streetTypeVO.setName(serviceType.getName());
            list.add(streetTypeVO);
        }
        return ok().pull("list", list).json();
    }

    @RequestMapping("/info")
    public String info(@RequestParam Integer id) {
        ConsumerVO consumerVO = consumerService.getById(id);
        return ok().pull("info", consumerVO).json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            consumerService.delete(id);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/import")
    public String importData(@RequestParam(value = "file") MultipartFile multipartFile) throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        String fileName = multipartFile.getOriginalFilename();
        InputStream in = multipartFile.getInputStream();
        try {
            List<ConsumerImport> list = new ReadDataUtil<ConsumerImport>().readData(in, fileName, new ConsumerImport());
            String re = consumerService.importData(list);
            return ok().pull("re", re).json();
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
    }
}
