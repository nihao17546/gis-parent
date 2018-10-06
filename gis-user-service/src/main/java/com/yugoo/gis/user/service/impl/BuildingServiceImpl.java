package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.StreetDAO;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.StreetPO;
import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IBuildingService;
import com.yugoo.gis.user.service.util.MapUtil;
import org.apache.ibatis.session.RowBounds;
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

    @Override
    public ListVO<BuildingVO> list(Integer curPage, Integer pageSize, String name) {
        long count = buildingDAO.selectCount(name);
        ListVO<BuildingVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<BuildingPO> poList = buildingDAO.select(name, new RowBounds((curPage - 1) * pageSize, pageSize));
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
    public void create(String name, Integer streetId, Double longitude, Double latitude) {
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
    }

    @Override
    public ListVO<BuildingVO> listByCenterId(Integer centerId) {
        CenterPO centerPO = centerDAO.selectById(centerId);
        List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
        List<BuildingPO> buildingPOList = buildingDAO.selectByLoAndLa(centerPO.getLoMin(), centerPO.getLoMax(), centerPO.getLaMin(), centerPO.getLaMax());
        ListVO<BuildingVO> listVO = new ListVO<>();
        List<BuildingVO> voList = new ArrayList<>();
        List<Integer> streetIds = new ArrayList<>();
        for (BuildingPO buildingPO : buildingPOList) {
            if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)) {
                BuildingVO vo = new BuildingVO();
                BeanUtils.copyProperties(buildingPO, vo);
                if (buildingPO.getStreetId() != null && !streetIds.contains(buildingPO.getStreetId())) {
                    streetIds.add(buildingPO.getStreetId());
                }
                voList.add(vo);
            }
        }
        if (!streetIds.isEmpty()) {
            Map<Integer,StreetPO> streetPOMap = streetDAO.selectByIds(streetIds);
            for (BuildingVO vo : voList) {
                if (streetPOMap.containsKey(vo.getStreetId())) {
                    vo.setStreetName(streetPOMap.get(vo.getStreetId()).getName());
                }
            }
        }
        listVO.setList(voList);
        listVO.setCurPage(1);
        listVO.setPageSize(Integer.MAX_VALUE);
        listVO.setTotalCount(voList.size());
        return listVO;
    }
}
