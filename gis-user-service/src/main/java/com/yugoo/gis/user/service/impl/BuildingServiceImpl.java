package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.ConsumerDAO;
import com.yugoo.gis.dao.ResourceDAO;
import com.yugoo.gis.dao.StreetDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.StreetPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IBuildingService;
import com.yugoo.gis.user.service.cache.ReferenceCache;
import com.yugoo.gis.user.service.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nihao
 * @create 2018/10/5
 **/
@Service
public class BuildingServiceImpl implements IBuildingService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BuildingDAO buildingDAO;
    @Autowired
    private StreetDAO streetDAO;
    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ConsumerDAO consumerDAO;
    @Autowired
    private ResourceDAO resourceDAO;
    @Autowired
    private ReferenceCache referenceCache;

    @Override
    public ListVO<BuildingVO> list(Integer curPage, Integer pageSize, String name, Integer streetId) {
        long count = buildingDAO.selectCount(name, streetId);
        ListVO<BuildingVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<BuildingPO> poList = buildingDAO.select(name, streetId, (curPage - 1) * pageSize, pageSize);
            List<Integer> streetIds = new ArrayList<>();
            List<BuildingVO> voList = poList.stream().map(po -> {
                BuildingVO vo = new BuildingVO();
                BeanUtils.copyProperties(po, vo);
                if (po.getStreetId() != null && !streetIds.contains(po.getStreetId())) {
                    streetIds.add(po.getStreetId());
                }
                return vo;
            }).collect(Collectors.toList());
            if (!streetIds.isEmpty()) {
                Map<Integer,StreetPO> streetPOMap = streetDAO.selectByIds(streetIds);
                for (BuildingVO vo : voList) {
                    if (streetPOMap.containsKey(vo.getStreetId())) {
                        vo.setStreetName(streetPOMap.get(vo.getStreetId()).getName());
                    }
                }
            }
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Transactional
    @Override
    public Integer create(String name, Integer streetId, Double longitude, Double latitude) {
        BuildingPO check = buildingDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        BuildingPO buildingPO = new BuildingPO();
        buildingPO.setName(name);
        buildingPO.setStreetId(streetId);
        buildingPO.setLongitude(longitude);
        buildingPO.setLatitude(latitude);
        buildingDAO.insert(buildingPO);
        BuildingPO avg = buildingDAO.selectAvgByStreetId(streetId);
        streetDAO.updateLoAndLa(streetId, avg.getLongitude(), avg.getLatitude());
        return buildingPO.getId();
    }

    @Transactional
    @Override
    public void update(Integer id, String name, Integer streetId, Double longitude, Double latitude) {
        BuildingPO check = buildingDAO.selectByName(name);
        if (check != null && !check.getId().equals(id)) {
            throw new GisRuntimeException("该名称已经存在");
        }
        BuildingPO oldBuildingPO = buildingDAO.selectById(id);
        BuildingPO buildingPO = new BuildingPO();
        buildingPO.setId(id);
        buildingPO.setName(name);
        buildingPO.setStreetId(streetId);
        buildingPO.setLongitude(longitude);
        buildingPO.setLatitude(latitude);
        buildingDAO.update(buildingPO);
        if (!oldBuildingPO.getStreetId().equals(streetId)) {
            BuildingPO avg1 = buildingDAO.selectAvgByStreetId(streetId);
            streetDAO.updateLoAndLa(streetId, avg1.getLongitude(), avg1.getLatitude());
            BuildingPO avg2 = buildingDAO.selectAvgByStreetId(oldBuildingPO.getStreetId());
            if (avg2 != null) {
                streetDAO.updateLoAndLa(oldBuildingPO.getStreetId(), avg2.getLongitude(), avg2.getLatitude());
            }
            else {
                streetDAO.updateLoAndLa(oldBuildingPO.getStreetId(), -999.0, -999.0);
            }
        }
        else if (!oldBuildingPO.getLongitude().equals(longitude)
                || !oldBuildingPO.getLatitude().equals(latitude)) {
            BuildingPO avg = buildingDAO.selectAvgByStreetId(streetId);
            streetDAO.updateLoAndLa(streetId, avg.getLongitude(), avg.getLatitude());
        }
        // 更新客户、资源的经纬度
        if (!oldBuildingPO.getLongitude().equals(longitude)
                || !oldBuildingPO.getLatitude().equals(latitude)) {
            consumerDAO.updateLoAndLaByBuildingId(id, longitude, latitude);
            resourceDAO.updateLoAndLaByBuildingId(id, longitude, latitude);
        }
    }

    @Override
    public ListVO<BuildingVO> listByCenterId(Integer centerId, Integer curPage, Integer pageSize) {
        CenterPO centerPO = centerDAO.selectById(centerId);
        if (centerPO == null)
            throw new GisRuntimeException("营销中心[" + centerId + "]不存在");
        return referenceCache.getBuildingByCenter(centerPO, curPage, pageSize);
    }

    @Override
    public List<BuildingVO> listOwn(Integer userId, String name, Integer limit) {
        UserPO userPO = userDAO.selectById(userId);
        List<BuildingPO> buildingPOList = null;
        if (userPO.getRole() == Role.admin.getValue()) {
            buildingPOList = buildingDAO.selectByLoAndLaAndName(null, null, null, null, name, limit);
        }
        else if (userPO.getRole() == Role.headman.getValue()) {
            List<CenterPO> centerPOList = centerDAO.selectByGroupId(userPO.getGroupId());
            buildingPOList = new ArrayList<>();
            for (CenterPO centerPO : centerPOList) {
                List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
                List<BuildingPO> buildingPOS = buildingDAO.selectByLoAndLaAndName(centerPO.getLoMin(), centerPO.getLoMax(),
                        centerPO.getLaMin(), centerPO.getLaMax(), name, limit);
                for (BuildingPO buildingPO : buildingPOS) {
                    if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)) {
                        buildingPOList.add(buildingPO);
                    }
                }
            }
        }
        else {
            buildingPOList = new ArrayList<>();
            CenterPO centerPO = centerDAO.selectById(userPO.getCenterId());
            List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
            List<BuildingPO> buildingPOS = buildingDAO.selectByLoAndLaAndName(centerPO.getLoMin(), centerPO.getLoMax(),
                    centerPO.getLaMin(), centerPO.getLaMax(), name, limit);
            for (BuildingPO buildingPO : buildingPOS) {
                if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)) {
                    buildingPOList.add(buildingPO);
                }
            }
        }
        List<BuildingVO> buildingVOList = buildingPOList.stream().map(po -> {
            BuildingVO vo = new BuildingVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return buildingVOList;
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        long l1 = consumerDAO.selectCountByBuildingId(id);
        if (l1 > 0L) {
            throw new GisRuntimeException("该建筑下关联有客户信息，不能删除");
        }
        long l2 = resourceDAO.selectCountByBuildingId(id);
        if (l2 > 0L) {
            throw new GisRuntimeException("该建筑下关联有网络资源，不能删除");
        }
        BuildingPO oldBuildingPO = buildingDAO.selectById(id);
        buildingDAO.deleteById(id);
        if (oldBuildingPO.getStreetId() != null && oldBuildingPO.getStreetId() != 0) {
            BuildingPO avg = buildingDAO.selectAvgByStreetId(oldBuildingPO.getStreetId());
            if (avg != null) {
                streetDAO.updateLoAndLa(oldBuildingPO.getStreetId(), avg.getLongitude(), avg.getLatitude());
            }
            else {
                streetDAO.updateLoAndLa(oldBuildingPO.getStreetId(), -999.0, -999.0);
            }
        }
    }

    @Override
    public List<BuildingVO> searchByName(String name) {
        List<BuildingPO> buildingPOList = buildingDAO.selectLikeName(name);
        List<BuildingVO> buildingVOList = buildingPOList.stream().map(po -> {
            BuildingVO vo = new BuildingVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return buildingVOList;
    }

    @Override
    public List<BuildingVO> searchFromMap(String name, Double loMin, Double loMax, Double laMin, Double laMax) {
        List<BuildingPO> buildingPOList = buildingDAO.selectFromMap(name, loMin, loMax, laMin, laMax, 50);
        List<BuildingVO> buildingVOList = buildingPOList.stream().map(po -> {
            BuildingVO vo = new BuildingVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return buildingVOList;
    }

    @Override
    public BuildingVO getById(Integer id) {
        BuildingPO buildingPO = buildingDAO.selectById(id);
        BuildingVO vo = new BuildingVO();
        BeanUtils.copyProperties(buildingPO, vo);
        if (buildingPO.getStreetId() != null && buildingPO.getStreetId() != 0) {
            StreetPO streetPO = streetDAO.selectById(buildingPO.getStreetId());
            if (streetPO != null) {
                vo.setStreetName(streetPO.getName());
            }
        }
        return vo;
    }
}
