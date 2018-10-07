package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.GroupDAO;
import com.yugoo.gis.pojo.po.GroupPO;
import com.yugoo.gis.pojo.vo.GroupListVO;
import com.yugoo.gis.pojo.vo.GroupVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IGroupService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author nihao 2018/9/20
 */
@RestController
@RequestMapping("/group")
public class GroupController extends BaseController {
    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private IGroupService groupService;

    @RequestMapping("/all")
    public String all() {
        RowBounds rowBounds = new RowBounds(0, Integer.MAX_VALUE);
        List<GroupPO> list = groupDAO.select(null, rowBounds);
        return ok().pull("list", list).json();
    }

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String name,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListVO<GroupListVO> listVO = groupService.list(curPage, pageSize, name);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            groupService.delete(id);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam String position) {
        GroupPO groupPO = new GroupPO();
        groupPO.setName(name);
        groupPO.setPosition(position);
        int a = groupDAO.insert(groupPO);
        if (a == 1) {
            return ok().json();
        }
        return fail("名称已存在").json();
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam String name, @RequestParam String position, @RequestParam Integer id) {
        GroupPO groupPO = new GroupPO();
        groupPO.setName(name);
        groupPO.setPosition(position);
        groupPO.setId(id);
        groupDAO.update(groupPO);
        return ok().json();
    }

    @RequestMapping("/info")
    public String info(@RequestParam Integer id) {
        GroupVO groupVO = groupService.getById(id);
        return ok().pull("info", groupVO).json();
    }
}
