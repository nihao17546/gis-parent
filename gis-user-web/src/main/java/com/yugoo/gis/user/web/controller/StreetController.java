package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.constant.StreetType;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StreetTypeVO;
import com.yugoo.gis.pojo.vo.StreetVO;
import com.yugoo.gis.user.service.IStreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/types")
    public String types() {
        List<StreetTypeVO> list = new ArrayList<>();
        for (StreetType streetType : StreetType.values()) {
            StreetTypeVO streetTypeVO = new StreetTypeVO();
            streetTypeVO.setId(streetType.getValue());
            streetTypeVO.setName(streetType.getName());
            list.add(streetTypeVO);
        }
        return ok().pull("list", list).json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam String position,
                         @RequestParam Integer type, @RequestParam String manager,
                         @RequestParam String phone, @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @RequestParam(required = false) String remark, @RequestParam String competitor) throws IOException {
        byte[] pic = null;
        if (multipartFile != null) {
            pic = multipartFile.getBytes();
        }
        try {
            streetService.create(name, position, type, manager, phone, pic, remark, competitor);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/edit")
    public String create(@RequestParam String name, @RequestParam String position,
                         @RequestParam Integer type, @RequestParam String manager,
                         @RequestParam String phone, @RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @RequestParam(required = false) String remark, @RequestParam String competitor,
                         @RequestParam Integer id, @RequestParam(required = false) byte[] pic) throws IOException {
        if (multipartFile != null) {
            pic = multipartFile.getBytes();
        }
        try {
            streetService.update(id, name, position, type, manager, phone, pic, remark, competitor);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/info")
    public String info(@RequestParam Integer id) {
        return ok().pull("info", streetService.getById(id)).json();
    }
}
