package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.GroupDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.GroupPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.PointVO;
import com.yugoo.gis.user.service.ICenterService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/9/28
 */
@Service
public class CenterServiceImpl implements ICenterService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public ListVO<CenterVO> list(Integer curPage, Integer pageSize, String name, Integer groupId) {
        long count = centerDAO.selectCount(name, groupId);
        ListVO<CenterVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<CenterPO> poList = centerDAO.select(name, groupId, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<Integer> groupIds = new ArrayList<>();
            List<CenterVO> voList = poList.stream().map(po -> {
                CenterVO vo = new CenterVO();
                BeanUtils.copyProperties(po, vo);
                if (vo.getRegion() != null && !vo.getRegion().equals("")) {
                    List<List<Double>> lists = JSON.parseObject(vo.getRegion(), new TypeReference<List<List<Double>>>(){});
                    List<PointVO> pointVOList = new ArrayList<>(lists.size());
                    for (List<Double> doubles : lists) {
                        PointVO pointVO = new PointVO();
                        pointVO.setLongitude(doubles.get(0));
                        pointVO.setLatitude(doubles.get(1));
                        pointVOList.add(pointVO);
                    }
                    vo.setPoints(pointVOList);
                }
                if (!groupIds.contains(vo.getGroupId())) {
                    groupIds.add(vo.getGroupId());
                }
                return vo;
            }).collect(Collectors.toList());
            Map<Integer,GroupPO> groupPOMap = new HashMap<>();
            if (!groupIds.isEmpty()) {
                groupPOMap = groupDAO.selectByIds(groupIds);
            }
            for (CenterVO centerVO : voList) {
                if (groupPOMap.containsKey(centerVO.getGroupId())) {
                    centerVO.setGroupName(groupPOMap.get(centerVO.getGroupId()).getName());
                }
            }
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void delete(Integer id) {
        List<UserPO> userPOList = userDAO.selectByCenterId(id);
        if (!userPOList.isEmpty()) {
            throw new GisRuntimeException("该营销中心关联有用户，不能删除");
        }
        centerDAO.deleteById(id);
    }

    @Override
    public void create(String name, Integer groupId, String manager, String phone, String position, String district,
                       String region, Double loMax, Double loMin, Double laMax, Double laMin) {
        CenterPO check = centerDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        CenterPO centerPO = new CenterPO();
        centerPO.setName(name);
        centerPO.setGroupId(groupId);
        centerPO.setManager(manager);
        centerPO.setPhone(phone);
        centerPO.setPosition(position);
        centerPO.setDistrict(district);
        centerPO.setRegion(region);
        if (loMax == null) {
            List<List<Double>> lists = JSON.parseObject(region, new TypeReference<List<List<Double>>>(){});
            for (List<Double> list : lists) {
                // 最大经度
                loMax = loMin == null ? list.get(0) : (list.get(0) > loMax ? list.get(0) : loMax);
                // 最大纬度
                laMax = laMax == null ? list.get(1) : (list.get(1) > laMax ? list.get(1) : laMax);
                // 最小经度
                loMin = loMin == null ? list.get(0) : (list.get(0) < loMin ? list.get(0) : loMin);
                // 最小纬度
                laMin = laMin == null ? list.get(1) : (list.get(1) < laMin ? list.get(1) : laMin);
            }
        }
        centerPO.setLoMax(loMax);
        centerPO.setLoMin(loMin);
        centerPO.setLaMax(laMax);
        centerPO.setLaMin(laMin);
        centerDAO.insert(centerPO);
    }

    @Override
    public void update(Integer id, String name, Integer groupId, String manager, String phone, String position,
                       String district, String region ,Double loMax, Double loMin, Double laMax, Double laMin) {
        CenterPO check = centerDAO.selectByName(name);
        if (check != null && !check.getId().equals(id)) {
            throw new GisRuntimeException("该名称已经存在");
        }
        CenterPO centerPO = new CenterPO();
        centerPO.setName(name);
        centerPO.setGroupId(groupId);
        centerPO.setManager(manager);
        centerPO.setPhone(phone);
        centerPO.setPosition(position);
        centerPO.setDistrict(district);
        centerPO.setRegion(region);
        if (loMax == null) {
            List<List<Double>> lists = JSON.parseObject(region, new TypeReference<List<List<Double>>>(){});
            for (List<Double> list : lists) {
                // 最大经度
                loMax = loMin == null ? list.get(0) : (list.get(0) > loMax ? list.get(0) : loMax);
                // 最大纬度
                laMax = laMax == null ? list.get(1) : (list.get(1) > laMax ? list.get(1) : laMax);
                // 最小经度
                loMin = loMin == null ? list.get(0) : (list.get(0) < loMin ? list.get(0) : loMin);
                // 最小纬度
                laMin = laMin == null ? list.get(1) : (list.get(1) < laMin ? list.get(1) : laMin);
            }
        }
        centerPO.setLoMax(loMax);
        centerPO.setLoMin(loMin);
        centerPO.setLaMax(laMax);
        centerPO.setLaMin(laMin);
        centerPO.setId(id);
        centerDAO.update(centerPO);
    }

    @Override
    public List<CenterVO> searchByName(String name) {
        List<CenterPO> centerPOList = centerDAO.selectLikeName(name);
        List<CenterVO> centerVOList = centerPOList.stream().map(po -> {
            CenterVO vo = new CenterVO();
            BeanUtils.copyProperties(po, vo);
            if (vo.getRegion() != null && !vo.getRegion().equals("")) {
                List<List<Double>> lists = JSON.parseObject(vo.getRegion(), new TypeReference<List<List<Double>>>(){});
                List<PointVO> pointVOList = new ArrayList<>(lists.size());
                for (List<Double> doubles : lists) {
                    PointVO pointVO = new PointVO();
                    pointVO.setLongitude(doubles.get(0));
                    pointVO.setLatitude(doubles.get(1));
                    pointVOList.add(pointVO);
                }
                vo.setPoints(pointVOList);
            }
            return vo;
        }).collect(Collectors.toList());
        return centerVOList;
    }

    @Override
    public List<CenterVO> searchFromMap(String name, Double loMin, Double loMax, Double laMin, Double laMax, Integer groupId) {
        List<CenterPO> centerPOList = centerDAO.selectFromMap(name, loMin, loMax, laMin, laMax, groupId);
        List<CenterVO> centerVOList = centerPOList.stream().map(po -> {
            CenterVO vo = new CenterVO();
            BeanUtils.copyProperties(po, vo);
            if (vo.getRegion() != null && !vo.getRegion().equals("")) {
                List<List<Double>> lists = JSON.parseObject(vo.getRegion(), new TypeReference<List<List<Double>>>(){});
                List<PointVO> pointVOList = new ArrayList<>(lists.size());
                for (List<Double> doubles : lists) {
                    PointVO pointVO = new PointVO();
                    pointVO.setLongitude(doubles.get(0));
                    pointVO.setLatitude(doubles.get(1));
                    pointVOList.add(pointVO);
                }
                vo.setPoints(pointVOList);
            }
            return vo;
        }).collect(Collectors.toList());
        return centerVOList;
    }

    @Override
    public CenterVO getById(Integer id) {
        CenterPO centerPO = centerDAO.selectById(id);
        CenterVO vo = new CenterVO();
        BeanUtils.copyProperties(centerPO, vo);
        if (vo.getRegion() != null && !vo.getRegion().equals("")) {
            List<List<Double>> lists = JSON.parseObject(vo.getRegion(), new TypeReference<List<List<Double>>>(){});
            List<PointVO> pointVOList = new ArrayList<>(lists.size());
            for (List<Double> doubles : lists) {
                PointVO pointVO = new PointVO();
                pointVO.setLongitude(doubles.get(0));
                pointVO.setLatitude(doubles.get(1));
                pointVOList.add(pointVO);
            }
            vo.setPoints(pointVOList);
        }
        if (centerPO.getGroupId() != null && centerPO.getGroupId() != 0) {
            GroupPO groupPO = groupDAO.selectById(centerPO.getGroupId());
            if (groupPO != null) {
                vo.setGroupName(groupPO.getName());
            }
        }
        return vo;
    }
}
