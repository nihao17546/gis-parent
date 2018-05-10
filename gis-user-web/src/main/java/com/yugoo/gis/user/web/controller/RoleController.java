package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.dao.RoleDAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by nihao on 18/5/10.
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Resource
    private RoleDAO roleDAO;

    @RequestMapping("/list")
    public String list(){
        return ok().pull("list", roleDAO.selectAll()).json();
    }
}
