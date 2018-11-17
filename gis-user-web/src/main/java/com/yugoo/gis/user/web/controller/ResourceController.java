package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.ResourceDAO;
import com.yugoo.gis.pojo.excel.ResourceImport;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.ResourcePO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;
import com.yugoo.gis.user.service.IResourceService;
import com.yugoo.gis.user.web.utils.ExcelUtil;
import com.yugoo.gis.user.web.utils.ReadDataUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.yugoo.gis.user.web.utils.ExcelUtil.resourceTitles;

/**
 * @author nihao 2018/10/23
 */
@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController {

    @Autowired
    private IResourceService resourceService;
    @Autowired
    private BuildingDAO buildingDAO;
    @Autowired
    private ResourceDAO resourceDAO;

    @PostConstruct
    public void init() {

    }

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String buildingName,
                       @RequestParam(required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) Integer buildingId) {
        if (buildingName != null) {
            buildingName = buildingName.trim();
            BuildingPO buildingPO = buildingDAO.selectByName(buildingName);
            if (buildingPO != null) {
                buildingId = buildingPO.getId();
            }
            else {
                return ok().pull("data", new ListVO<>(curPage, pageSize)).json();
            }
        }
        ListVO<ResourceVO> listVO = resourceService.list(curPage, pageSize, buildingId);
        return ok().pull("data", listVO).json();
    }

    @RequestMapping("/create")
    public String create(@RequestParam(required = false) Integer buildingId,
                         @RequestParam(required = false) String district,
                         @RequestParam(required = false) String cityName,
                         @RequestParam(required = false) String streetName,
                         @RequestParam(required = false) String villageName,
                         @RequestParam(required = false) String admStreetName,
                         @RequestParam(required = false) String zoneName,
                         @RequestParam String floor,
                         @RequestParam String number,
                         @RequestParam(required = false) Integer allPortCount,
                         @RequestParam(required = false) Integer idelPortCount,
                         @RequestParam(required = false) String sceneA,
                         @RequestParam(required = false) String sceneB,
                         @RequestParam(required = false) String overlayScene,
                         @RequestParam(required = false) Double longitude,
                         @RequestParam(required = false) Double latitude) {
        try {
            resourceService.create(buildingId, district, floor, number, allPortCount, idelPortCount,
                    sceneA, sceneB, overlayScene, longitude, latitude, cityName, streetName,
                    villageName, admStreetName, zoneName);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/edit")
    public String create(@RequestParam Integer buildingId,
                         @RequestParam(required = false) String district,
                         @RequestParam(required = false) String cityName,
                         @RequestParam(required = false) String streetName,
                         @RequestParam(required = false) String villageName,
                         @RequestParam(required = false) String admStreetName,
                         @RequestParam(required = false) String zoneName,
                         @RequestParam String floor,
                         @RequestParam String number,
                         @RequestParam(required = false) Integer allPortCount,
                         @RequestParam(required = false) Integer idelPortCount,
                         @RequestParam(required = false) String sceneA,
                         @RequestParam(required = false) String sceneB,
                         @RequestParam(required = false) String overlayScene,
                         @RequestParam Integer id) {
        try {
            resourceService.update(id, buildingId, district, floor, number, allPortCount, idelPortCount,
                    sceneA, sceneB, overlayScene, cityName, streetName, villageName, admStreetName, zoneName);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id) {
        try {
            resourceService.delete(id);
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
            List<ResourceImport> list = new ReadDataUtil<ResourceImport>().readData(in, fileName, new ResourceImport());
            if (list.isEmpty()) {
                return fail("没有数据").json();
            }
            String re = resourceService.importData(list);
            return ok().pull("re", re).json();
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
    }

    @RequestMapping(value = "/export",method = RequestMethod.POST)
    public void export(HttpServletRequest request, HttpServletResponse response,
                       @RequestParam(required = false) String buildingName,
                       @RequestParam(required = false) Integer buildingId) throws NoSuchFieldException, IllegalAccessException {
        if (buildingName != null) {
            buildingName = buildingName.trim();
            BuildingPO buildingPO = buildingDAO.selectByName(buildingName);
            if (buildingPO != null) {
                buildingId = buildingPO.getId();
            }
            else {
                Workbook workbook = ExcelUtil.prepareExport(null, resourceTitles);
                ExcelUtil.export(request, response, workbook, "网络资源");
                return;
            }
        }
        ListVO<ResourceVO> listVO = resourceService.list(1, 5000, buildingId);
        Workbook workbook = ExcelUtil.prepareExport(listVO.getList(), resourceTitles);
        ExcelUtil.export(request, response, workbook, "网络资源");
    }


}
