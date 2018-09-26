package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/all")
    public String all(@RequestParam Integer groupId) {
        List<CenterPO> list = centerDAO.selectAll(groupId);
        return ok().pull("list", list).json();
    }
}
