package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @create 2018/10/5
 **/
@RestController
@RequestMapping("/building")
public class BuildingController extends BaseController {

    @Autowired
    private IBuildingService buildingService;

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String name,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) Integer centerId) {
        ListVO<BuildingVO> listVO = null;
        if (centerId == null || centerId == 0) {
            listVO = buildingService.list(curPage, pageSize, name);
        }
        else {
            listVO = buildingService.listByCenterId(centerId);
        }
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam Integer streetId,
                         @RequestParam Double longitude, @RequestParam Double latitude) {
        try {
            buildingService.create(name, streetId, longitude, latitude);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/edit")
    public String create(@RequestParam String name, @RequestParam Integer streetId,
                         @RequestParam Double longitude, @RequestParam Double latitude,
                         @RequestParam Integer id) {
        try {
            buildingService.update(id, name, streetId, longitude, latitude);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }
}
