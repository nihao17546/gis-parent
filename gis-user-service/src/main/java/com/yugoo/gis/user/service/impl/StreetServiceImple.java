package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.constant.StreetType;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.StreetDAO;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.StreetPO;
import com.yugoo.gis.pojo.vo.CompetitorVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.PointVO;
import com.yugoo.gis.pojo.vo.StreetVO;
import com.yugoo.gis.user.service.IStreetService;
import com.yugoo.gis.user.service.util.MapUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/9/30
 */
@Service
public class StreetServiceImple implements IStreetService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StreetDAO streetDAO;
    @Autowired
    private BuildingDAO buildingDAO;
    @Autowired
    private CenterDAO centerDAO;

    @Override
    public ListVO<StreetVO> list(Integer curPage, Integer pageSize, String name) {
        long count = streetDAO.selectCount(name);
        ListVO<StreetVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<StreetPO> streetPOList = streetDAO.select(name, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<Integer> streetIds = new ArrayList<>();
            List<StreetVO> streetVOList = streetPOList.stream().map(po -> {
                StreetVO vo = new StreetVO();
                BeanUtils.copyProperties(po, vo);
                vo.setTypeName(StreetType.getByValue(vo.getType()).getName());
                if (po.getCompetitor() != null && !po.getCompetitor().equals("")) {
                    List<CompetitorVO> competitors = JSON.parseObject(po.getCompetitor(),
                            new TypeReference<List<CompetitorVO>>(){});
                    vo.setCompetitors(competitors);
                }
                if (!streetIds.contains(po.getId())) {
                    streetIds.add(po.getId());
                }
                return vo;
            }).collect(Collectors.toList());
            List<BuildingPO> buildingPOList = buildingDAO.selectByStreetIds(streetIds);
            for (StreetVO vo : streetVOList) {
                List<PointVO> pointVOS = new ArrayList<>();
                for (int i = buildingPOList.size() - 1; i >= 0; i --) {
                    if (buildingPOList.get(i).getStreetId().equals(vo.getId())) {
                        PointVO pointVO = new PointVO();
                        pointVO.setName(buildingPOList.get(i).getName());
                        pointVO.setLongitude(buildingPOList.get(i).getLongitude());
                        pointVO.setLatitude(buildingPOList.get(i).getLatitude());
                        pointVOS.add(pointVO);
                        buildingPOList.remove(i);
                    }
                }
                vo.setBuildingPoints(pointVOS);
            }
            listVO.setList(streetVOList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public Integer create(String name, String position, Integer type, String manager, String phone, byte[] pic, String remark, String competitor) {
        StreetPO check = streetDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        StreetPO streetPO = new StreetPO();
        streetPO.setName(name);
        streetPO.setPosition(position);
        streetPO.setType(type);
        streetPO.setManager(manager);
        streetPO.setPhone(phone);
        streetPO.setPic(pic);
        streetPO.setRemark(remark);
        streetPO.setCompetitor(competitor);
        streetDAO.insert(streetPO);
        return streetPO.getId();
    }

    @Override
    public void update(Integer id, String name, String position, Integer type, String manager, String phone, byte[] pic, String remark, String competitor) {
        StreetPO check = streetDAO.selectByName(name);
        if (check != null && !check.getId().equals(id)) {
            throw new GisRuntimeException("该名称已经存在");
        }
        StreetPO streetPO = new StreetPO();
        streetPO.setId(id);
        streetPO.setName(name);
        streetPO.setPosition(position);
        streetPO.setType(type);
        streetPO.setManager(manager);
        streetPO.setPhone(phone);
        streetPO.setPic(pic);
        streetPO.setRemark(remark);
        streetPO.setCompetitor(competitor);
        streetDAO.update(streetPO);
    }

    @Override
    public StreetVO getById(Integer id) {
        StreetPO po = streetDAO.selectById(id);
        StreetVO vo = new StreetVO();
        BeanUtils.copyProperties(po, vo);
        vo.setTypeName(StreetType.getByValue(vo.getType()).getName());
        if (po.getCompetitor() != null && !po.getCompetitor().equals("")) {
            List<CompetitorVO> competitors = JSON.parseObject(po.getCompetitor(),
                    new TypeReference<List<CompetitorVO>>(){});
            vo.setCompetitors(competitors);
        }
        return vo;
    }

    @Override
    public List<StreetVO> selectByCenterId(Integer centerId) {
        CenterPO centerPO = centerDAO.selectById(centerId);
        if (centerPO == null)
            throw new GisRuntimeException("营销中心[" + centerId + "]不存在");
        List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
        List<StreetPO> streetPOList = streetDAO.selectByLoAndLa(centerPO.getLoMin(), centerPO.getLoMax(), centerPO.getLaMin(), centerPO.getLaMax());
        List<StreetVO> list = new ArrayList<>();
        for (StreetPO streetPO : streetPOList) {
            if (MapUtil.isPtInPoly(streetPO.getLongitude(), streetPO.getLatitude(), lists)) {
                StreetVO vo = new StreetVO();
                BeanUtils.copyProperties(streetPO, vo);
                vo.setTypeName(StreetType.getByValue(vo.getType()).getName());
                if (streetPO.getCompetitor() != null && !streetPO.getCompetitor().equals("")) {
                    List<CompetitorVO> competitors = JSON.parseObject(streetPO.getCompetitor(),
                            new TypeReference<List<CompetitorVO>>(){});
                    vo.setCompetitors(competitors);
                }
                list.add(vo);
            }
        }
        return list;
    }

    @Override
    public void delete(Integer id) {
        List<BuildingPO> buildingPOList = buildingDAO.selectByStreetId(id);
        if (!buildingPOList.isEmpty()) {
            throw new GisRuntimeException("该物业街道关联有建筑，不能删除");
        }
        streetDAO.deleteById(id);
    }
}
