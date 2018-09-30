package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StreetVO;
import com.yugoo.gis.user.service.IStreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao 2018/9/30
 */
@RestController
@RequestMapping("/street")
public class StreetController extends BaseController {
    @Autowired
    private IStreetService streetService;

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String name,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ListVO<StreetVO> listVO = streetService.list(curPage, pageSize, name);
        return ok().pull("data", listVO).json();
    }
}
