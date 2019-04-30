package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
                       @RequestParam(required = false) Integer centerId,
                       @RequestParam(required = false) Integer streetId,
                       @RequestParam(required = false) Integer id) {
        ListVO<BuildingVO> listVO = null;
        if (id == null || id == 0) {
            if (centerId == null || centerId == 0) {
                if (streetId != null && streetId == 0)
                    streetId = null;
                listVO = buildingService.list(curPage, pageSize, name, streetId);
            }
            else {
                listVO = buildingService.listByCenterId(centerId, curPage, pageSize);
            }
        }
        else {
            BuildingVO buildingVO = buildingService.getById(id);
            listVO = new ListVO<>();
            listVO.setCurPage(1);
            listVO.setPageSize(10);
            listVO.setTotalPage(1);
            listVO.setTotalCount(1);
            List<BuildingVO> list = new ArrayList<>();
            list.add(buildingVO);
            listVO.setList(list);
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

    @RequestMapping("/listOwn")
    public String listOwn(@Value("#{request.getAttribute('uid')}") Integer uid,
                          @RequestParam(required = false) String name) {
        return ok().pull("list", buildingService.listOwn(uid, name, 20)).json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            buildingService.delete(id);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/mapSearch")
    public String mapSearch(@RequestParam String name) {
        List<BuildingVO> list = buildingService.searchByName(name);
        return ok().pull("list", list).json();
    }
}
