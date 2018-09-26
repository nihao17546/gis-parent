package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.pojo.vo.RoleVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nihao 2018/9/26
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @RequestMapping("/all")
    public String all() {
        List<RoleVO> list = new ArrayList<>();
        for (Role role : Role.values()) {
            RoleVO roleVO = new RoleVO();
            roleVO.setId(role.getValue());
            roleVO.setName(role.getName());
            list.add(roleVO);
        }
        return ok().pull("list", list).json();
    }

}
