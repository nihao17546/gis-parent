package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ConsumerVO;
import com.yugoo.gis.user.service.IBuildingService;
import com.yugoo.gis.user.service.ICenterService;
import com.yugoo.gis.user.service.IConsumerService;
import com.yugoo.gis.user.web.map.MapSearchResult;
import com.yugoo.gis.user.web.map.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author nihao 2018/10/26
 */
@RestController
@RequestMapping("/map")
public class MapController extends BaseController {
    @Autowired
    private MapService mapService;
    @Autowired
    private ICenterService centerService;
    @Autowired
    private IBuildingService buildingService;
    @Autowired
    private IConsumerService consumerService;
    @Autowired
    private UserDAO userDAO;

    @RequestMapping("/search")
    public String search(@RequestParam String query,
                         @RequestParam double minLongitude,
                         @RequestParam double maxLongitude,
                         @RequestParam double minLatitude,
                         @RequestParam double maxLatitude,
                         @Value("#{request.getAttribute('uid')}") Integer uid,
                         @Value("#{request.getAttribute('role')}") Integer role,
                         @RequestParam(required = false, defaultValue = "false") Boolean center,
                         @RequestParam(required = false, defaultValue = "false") Boolean building,
                         @RequestParam(required = false, defaultValue = "false") Boolean consumer,
                         @RequestParam(required = false, defaultValue = "false") Boolean baidu) {
        List<CenterVO> centerVOList = new ArrayList<>();
        List<BuildingVO> buildingVOList = new ArrayList<>();
        List<ConsumerVO> consumerVOList = new ArrayList<>();
        List<MapSearchResult> mapSearchResultList = new ArrayList<>();
        UserPO currentUser = userDAO.selectById(uid);
        if (center) {
            Integer groupId = null;
            if (currentUser.getRole() != Role.admin.getValue()) {
                groupId = currentUser.getGroupId();
            }
            centerVOList = centerService.searchFromMap(query, minLongitude, maxLongitude, minLatitude, maxLatitude, groupId);
        }
        if (building) {
            buildingVOList = buildingService.searchFromMap(query, minLongitude, maxLongitude, minLatitude, maxLatitude);
        }
        if (consumer) {
            consumerVOList = consumerService.searchFromMap(query, minLongitude, maxLongitude, minLatitude, maxLatitude, currentUser);
        }
        if (baidu) {
            mapSearchResultList = mapService.search(query, minLatitude, minLongitude, maxLatitude, maxLongitude);
        }
        return ok().pull("center", centerVOList)
                .pull("building", buildingVOList)
                .pull("baidu", mapSearchResultList)
                .pull("consumer", consumerVOList)
                .json();
    }
}
