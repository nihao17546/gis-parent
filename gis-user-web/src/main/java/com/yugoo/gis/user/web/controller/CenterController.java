package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.ICenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nihao 2018/9/26
 */
@RestController
@RequestMapping("/center")
public class CenterController extends BaseController {

    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private ICenterService centerService;
    @Autowired
    private UserDAO userDAO;

    @RequestMapping("/all")
    public String all(@RequestParam Integer groupId) {
        List<CenterPO> list = centerDAO.selectAll(groupId);
        return ok().pull("list", list).json();
    }

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) Integer groupId,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @Value("#{request.getAttribute('uid')}") Integer uid,
                       @Value("#{request.getAttribute('role')}") Integer role) {
        if ((groupId == null || groupId == 0) && role != Role.admin.getValue()) {
            UserPO currentUser = userDAO.selectById(uid);
            groupId = currentUser.getGroupId();
        }
        ListVO<CenterVO> listVO = centerService.list(curPage, pageSize, name, groupId);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            centerService.delete(id);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam(required = false) Integer groupId,
                         @RequestParam String manager, @RequestParam String phone,
                         @RequestParam String position, @RequestParam String district,
                         @RequestParam String region, @RequestParam(required = false) Double loMax,
                         @RequestParam(required = false) Double loMin, @RequestParam(required = false) Double laMax,
                         @RequestParam(required = false) Double laMin) {
        try {
            centerService.create(name, groupId, manager, phone, position, district, region, loMax, loMin, laMax, laMin);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam String name, @RequestParam(required = false) Integer groupId,
                       @RequestParam String manager, @RequestParam String phone,
                       @RequestParam String position, @RequestParam String district,
                       @RequestParam String region, @RequestParam Integer id,
                       @RequestParam(required = false) Double loMax, @RequestParam(required = false) Double loMin,
                       @RequestParam(required = false) Double laMax, @RequestParam(required = false) Double laMin) {
        try {
            centerService.update(id, name, groupId, manager, phone, position, district, region, loMax, loMin, laMax, laMin);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }
}
