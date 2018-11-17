package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.ResourceDAO;
import com.yugoo.gis.dao.StreetDAO;
import com.yugoo.gis.pojo.excel.ResourceImport;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.ResourcePO;
import com.yugoo.gis.pojo.po.StreetPO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;
import com.yugoo.gis.user.service.IBuildingService;
import com.yugoo.gis.user.service.IResourceService;
import com.yugoo.gis.user.service.IStreetService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/10/23
 */
@Service
public class ResourceServiceImpl implements IResourceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ResourceDAO resourceDAO;
    @Autowired
    private BuildingDAO buildingDAO;
    @Autowired
    private StreetDAO streetDAO;
    @Autowired
    private IStreetService streetService;
    @Autowired
    private IBuildingService buildingService;

    @Override
    public void create(Integer buildingId, String district, String floor, String number,
                       Integer allPortCount, Integer idelPortCount, String sceneA,
                       String sceneB, String overlayScene, Double longitude, Double latitude,
                       String cityName, String streetName, String villageName, String admStreetName,
                       String zoneName) {
        if (allPortCount == null) allPortCount = 0;
        if (idelPortCount == null) idelPortCount = 0;
        if (idelPortCount > allPortCount) {
            throw new GisRuntimeException("空闲端口数不能大于总端口数");
        }
        // 如果关联有建筑
        if (buildingId != null && buildingId != 0) {
            BuildingPO buildingPO = buildingDAO.selectById(buildingId);
            longitude = buildingPO.getLongitude();
            latitude = buildingPO.getLatitude();
        }
        // 如果没有关联建筑，且没有经纬度
        else if(longitude == null || latitude == null) {
            throw new GisRuntimeException("信息不完整，缺少经纬度坐标或未关联建筑");
        }
        // 如果没有关联建筑，但有经纬度
        else {
            buildingId = 0;
        }
        checkUnique(null, buildingId, floor, number, longitude, latitude);
        ResourcePO resourcePO = new ResourcePO();
        resourcePO.setBuildingId(buildingId);
        resourcePO.setDistrict(district);
        resourcePO.setCityName(cityName);
        resourcePO.setStreetName(streetName);
        resourcePO.setVillageName(villageName);
        resourcePO.setAdmStreetName(admStreetName);
        resourcePO.setZoneName(zoneName);
        resourcePO.setFloor(floor);
        resourcePO.setNumber(number);
        resourcePO.setAllPortCount(allPortCount);
        resourcePO.setIdelPortCount(idelPortCount);
        resourcePO.setSceneA(sceneA);
        resourcePO.setSceneB(sceneB);
        resourcePO.setOverlayScene(overlayScene);
        resourcePO.setLongitude(longitude);
        resourcePO.setLatitude(latitude);
        resourceDAO.insert(resourcePO);
    }

    private void checkUnique(Integer id, Integer buildingId, String floor, String number, Double longitude, Double latitude) {
        ResourcePO resourcePO = resourceDAO.selectByBuildingIdAndFloorAndNumber(buildingId, floor, number, longitude, latitude);
        if (resourcePO != null) {
            if (id == null || !id.equals(resourcePO.getId())) {
                throw new GisRuntimeException("该建筑下已存在相同楼层和户号的网络资源");
            }
        }
    }

    @Override
    public void update(Integer id, Integer buildingId, String district, String floor, String number,
                       Integer allPortCount, Integer idelPortCount, String sceneA, String sceneB,
                       String overlayScene,
                       String cityName, String streetName, String villageName, String admStreetName,
                       String zoneName) {
        if (allPortCount == null) allPortCount = 0;
        if (idelPortCount == null) idelPortCount = 0;
        if (idelPortCount > allPortCount) {
            throw new GisRuntimeException("空闲端口数不能大于总端口数");
        }
        if (buildingId == null || buildingId == 0) {
            throw new GisRuntimeException("信息不完整，未关联建筑");
        }
        BuildingPO buildingPO = buildingDAO.selectById(buildingId);
        Double longitude = buildingPO.getLongitude();
        Double latitude = buildingPO.getLatitude();
        checkUnique(id, buildingId, floor, number, longitude, latitude);

        ResourcePO resourcePO = new ResourcePO();
        resourcePO.setLongitude(longitude);
        resourcePO.setLatitude(latitude);
        resourcePO.setId(id);
        resourcePO.setBuildingId(buildingId);
        resourcePO.setDistrict(district);
        resourcePO.setCityName(cityName);
        resourcePO.setStreetName(streetName);
        resourcePO.setVillageName(villageName);
        resourcePO.setAdmStreetName(admStreetName);
        resourcePO.setZoneName(zoneName);
        resourcePO.setFloor(floor);
        resourcePO.setNumber(number);
        resourcePO.setAllPortCount(allPortCount);
        resourcePO.setIdelPortCount(idelPortCount);
        resourcePO.setSceneA(sceneA);
        resourcePO.setSceneB(sceneB);
        resourcePO.setOverlayScene(overlayScene);
        resourceDAO.update(resourcePO);
    }

    @Override
    public ListVO<ResourceVO> list(Integer curPage, Integer pageSize, Integer buildingId) {
        long count = resourceDAO.selectCount(buildingId);
        ListVO<ResourceVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<ResourcePO> resourcePOList = resourceDAO.select(buildingId, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<Integer> buildingIds = new ArrayList<>();
            List<ResourceVO> list = resourcePOList.stream().map(po -> {
                ResourceVO vo = new ResourceVO();
                BeanUtils.copyProperties(po, vo);
                if (po.getBuildingId() != null && po.getBuildingId() != 0
                        && !buildingIds.contains(po.getBuildingId())) {
                    buildingIds.add(po.getBuildingId());
                }
                return vo;
            }).collect(Collectors.toList());
            if (!buildingIds.isEmpty()) {
                Map<Integer,BuildingPO> buildingPOMap = buildingDAO.selectByIds(buildingIds);
                if (!buildingPOMap.isEmpty()) {
                    for (ResourceVO resourceVO : list) {
                        if (resourceVO.getBuildingId() != null
                                && resourceVO.getBuildingId() != 0
                                && buildingPOMap.containsKey(resourceVO.getBuildingId())) {
                            BuildingPO buildingPO = buildingPOMap.get(resourceVO.getBuildingId());
                            resourceVO.setBuildingName(buildingPO.getName());
                            resourceVO.setLongitude(buildingPO.getLongitude());
                            resourceVO.setLatitude(buildingPO.getLatitude());
                        }
                    }
                }
            }
            listVO.setList(list);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void delete(Integer id) {
        resourceDAO.delete(id);
    }

    private Map<String,Boolean> complete(ResourceImport resourceImport) {
        boolean street = false;
        boolean building = false;
        StringBuilder buildingNameSb = new StringBuilder();
        if (resourceImport.getStreetPOName() != null) {
            buildingNameSb.append(resourceImport.getStreetPOName());
        }
        if (resourceImport.getBuildingNameB() != null) {
            buildingNameSb.append(resourceImport.getBuildingNameB());
        }
        if (resourceImport.getBuildingNameC() != null) {
            buildingNameSb.append(resourceImport.getBuildingNameC());
        }
        String buildingName = buildingNameSb.toString();
        if (buildingName != null || !buildingName.equals("")) {
            resourceImport.setBuildingPOName(buildingName);
        }
        if (resourceImport.getBuildingPOName() == null) {
            throw new GisRuntimeException(resourceImport.getR() + "未能确定所属建筑（建筑由【小区/自然村/弄】【幢/号/楼】【单元号】三列确定）");
        }
        BuildingPO buildingPO = buildingDAO.selectByName(buildingName);
        if (buildingPO != null) {
            resourceImport.setBuildingId(buildingPO.getId());
            resourceImport.setLongitude(buildingPO.getLongitude());
            resourceImport.setLatitude(buildingPO.getLatitude());
        }
        else if (resourceImport.getStreetPOName() == null || resourceImport.getStreetPOName().equals("")){
            throw new GisRuntimeException(resourceImport.getR() + "未能确定所属物业街道（物业街道由【小区/自然村/弄】列确定）");
        }
        else {
            StreetPO streetPO = streetDAO.selectByName(resourceImport.getStreetPOName());
            Integer streetId = null;
            if (streetPO == null) {
                // 创建物业街道
                streetId = streetService.create(resourceImport.getStreetPOName(), "", 0,
                                "", "", null, null, "[]");
                street = true;
            }
            else {
                streetId = streetPO.getId();
            }
            // 创建建筑
            if (resourceImport.getLongitude() == null || resourceImport.getLatitude() == null) {
                throw new GisRuntimeException(resourceImport.getR() + "未能确定经纬度坐标（经纬度坐标由【中心位置经度】【中心位置纬度】两列确定）");
            }
            Integer buildingId = buildingService.create(buildingName, streetId,
                            resourceImport.getLongitude(), resourceImport.getLatitude());
            building = true;
            resourceImport.setBuildingId(buildingId);
        }
        if (resourceImport.getAllPortCount() == null) {
            throw new GisRuntimeException(resourceImport.getR() + "未能确定端口总数");
        }
        if (resourceImport.getIdelPortCount() == null) {
            throw new GisRuntimeException( resourceImport.getR() + "未能确定空余端口数");
        }
        if (resourceImport.getAllPortCount() < resourceImport.getIdelPortCount()) {
            throw new GisRuntimeException(resourceImport.getR() + "空余端口数不能大于总端口数");
        }
        if(resourceImport.getFloor() == null) {
            throw new GisRuntimeException(resourceImport.getR() + "未能确定楼层");
        }
        if(resourceImport.getNumber() == null) {
            throw new GisRuntimeException(resourceImport.getR() + "未能确定户号");
        }
        Map<String,Boolean> map = new HashMap<>();
        map.put("street", street);
        map.put("building", building);
        return map;
    }

    @Transactional
    @Override
    public String importData(List<ResourceImport> list) {
        int street = 0;
        int building = 0;
        List<ResourcePO> resourcePOList = new ArrayList<>();
        for (ResourceImport resourceImport : list) {
            Map<String,Boolean> map = complete(resourceImport);
            if (map.get("street")) {
                street ++;
            }
            if (map.get("building")) {
                building ++;
            }
            ResourcePO resourcePO = new ResourcePO();
            BeanUtils.copyProperties(resourceImport, resourcePO);
            resourcePOList.add(resourcePO);
        }
        int re = resourceDAO.batchInsert(resourcePOList);
        String ex = "";
        if (street > 0) {
            ex = ex + ",相关联物业街道新创建" + street + "条数据";
        }
        if (building > 0) {
            ex = ex + ",相关联建筑新创建" + building + "条数据";
        }
        return "总共导入网络资源" + resourcePOList.size() + "条数据(新增" + (2 * resourcePOList.size() - re)
                + "条,更新" + (re - resourcePOList.size()) + "条)" + ex;
    }

}
