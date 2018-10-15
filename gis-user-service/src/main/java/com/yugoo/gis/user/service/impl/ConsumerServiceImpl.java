package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.constant.ConsumerType;
import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.common.constant.ServiceType;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.SimpleDateUtil;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.ConsumerDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.ConsumerPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ConsumerVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IConsumerService;
import com.yugoo.gis.user.service.util.MapUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/10/9
 */
@Service
public class ConsumerServiceImpl implements IConsumerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConsumerDAO consumerDAO;
    @Autowired
    private BuildingDAO buildingDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CenterDAO centerDAO;

    @Override
    public void create(String name, Integer buildingId, String floor, String number, String position, byte[] pic,
                       String category, String nature, Integer peopleNum, String linkman, String phone, String operator,
                       BigDecimal expenses, Long expirationDate, String bandwidth, Integer serviceType, String status,
                       String legal, Integer lineNum, String lineType, Long lineOpenDate, String lineStatus, String groupCode,
                       String groupGrade, UserPO currentUser, Integer bindUserId) {
        ConsumerPO check = consumerDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        ConsumerPO consumerPO = new ConsumerPO();
        consumerPO.setName(name);
        consumerPO.setBuildingId(buildingId);
        consumerPO.setFloor(floor);
        consumerPO.setNumber(number);
        consumerPO.setPosition(position);
        consumerPO.setPic(pic);
        consumerPO.setCategory(category);
        consumerPO.setNature(nature);
        consumerPO.setPeopleNum(peopleNum);
        consumerPO.setLinkman(linkman);
        consumerPO.setPhone(phone);
        consumerPO.setOperator(operator);
        consumerPO.setExpenses(expenses);
        consumerPO.setExpirationDate(expirationDate);
        consumerPO.setBandwidth(bandwidth);
        consumerPO.setServiceType(serviceType);
        consumerPO.setStatus(status);
        consumerPO.setLegal(legal);
        consumerPO.setLineNum(lineNum);
        consumerPO.setLineType(lineType);
        consumerPO.setLineOpenDate(lineOpenDate);
        consumerPO.setLineStatus(lineStatus);
        consumerPO.setGroupCode(groupCode);
        consumerPO.setGroupGrade(groupGrade);

        if (isComplete(consumerPO)) {
            consumerPO.setType(ConsumerType.有效建档.getValue());
        }
        else if (isBasic(consumerPO)) {
            consumerPO.setType(ConsumerType.基础建档.getValue());
        }
        else {
            consumerPO.setType(ConsumerType.未建档.getValue());
        }

        if (consumerPO.getType() == ConsumerType.有效建档.getValue()
                || consumerPO.getType() == ConsumerType.基础建档.getValue()) {
            // 如果是客户经理创建的，绑定到本人身上
            if (currentUser.getRole() == Role.member.getValue()) {
                consumerPO.setUserId(currentUser.getId());
            }
            // 如果是组长或管理员创建的，绑定到bindUser
            else if (bindUserId != null){
                consumerPO.setUserId(bindUserId);
            }
            // 如果没有指明bindUser，不绑定
            else {
                consumerPO.setUserId(0);
            }
        }
        else {
            consumerPO.setUserId(0);
        }

        consumerDAO.insert(consumerPO);
    }

    @Override
    public ListVO<ConsumerListVO> list(Integer curPage, Integer pageSize, String name, UserPO currentUser) {
        List<Integer> buildingIdsParam = null;
        if (currentUser.getRole() != Role.admin.getValue()) {
            buildingIdsParam = new ArrayList<>();
            currentUser = userDAO.selectById(currentUser.getId());
            if (currentUser.getRole() == Role.headman.getValue()) {
                List<CenterPO> centerPOList = centerDAO.selectByGroupId(currentUser.getGroupId());
                for (CenterPO centerPO : centerPOList) {
                    List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
                    List<BuildingPO> buildingPOList = buildingDAO.selectByLoAndLa(
                            centerPO.getLoMin(), centerPO.getLoMax(), centerPO.getLaMin(), centerPO.getLaMax());
                    for (BuildingPO buildingPO : buildingPOList) {
                        if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)
                                && !buildingIdsParam.contains(buildingPO.getId())) {
                            buildingIdsParam.add(buildingPO.getId());
                        }
                    }
                }
            }
            else {
                CenterPO centerPO = centerDAO.selectById(currentUser.getCenterId());
                List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
                List<BuildingPO> buildingPOList = buildingDAO.selectByLoAndLa(
                        centerPO.getLoMin(), centerPO.getLoMax(), centerPO.getLaMin(), centerPO.getLaMax());
                for (BuildingPO buildingPO : buildingPOList) {
                    if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)
                            && !buildingIdsParam.contains(buildingPO.getId())) {
                        buildingIdsParam.add(buildingPO.getId());
                    }
                }
            }
        }

        long count = consumerDAO.selectCount(name, currentUser.getRole(), currentUser.getId(), buildingIdsParam);
        ListVO<ConsumerListVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<ConsumerPO> poList = consumerDAO.select(name, currentUser.getRole(), currentUser.getId(), buildingIdsParam,
                    new RowBounds((curPage - 1) * pageSize, pageSize));
            List<Integer> buildingIds = new ArrayList<>();
            List<Integer> userIds = new ArrayList<>();
            List<ConsumerListVO> voList = poList.stream().map(po -> {
                ConsumerListVO vo = new ConsumerListVO();
                BeanUtils.copyProperties(po, vo);
                vo.setTypeName(ConsumerType.getByValue(vo.getType()).getName());
                if (po.getServiceType() != null) {
                    vo.setServiceTypeName(ServiceType.getByValue(po.getServiceType()).getName());
                }
                if (po.getExpirationDate() != null) {
                    vo.setExpirationDateStr(SimpleDateUtil.shortFormat(new Date(po.getExpirationDate())));
                }
                if (po.getLineOpenDate() != null) {
                    vo.setLineOpenDateStr(SimpleDateUtil.shortFormat(new Date(po.getLineOpenDate())));
                }
                if (po.getBuildingId() != null && !buildingIds.contains(po.getBuildingId())) {
                    buildingIds.add(po.getBuildingId());
                }
                if (po.getUserId() != null && po.getUserId() != 0 && !userIds.contains(po.getUserId())) {
                    userIds.add(po.getUserId());
                }
                return vo;
            }).collect(Collectors.toList());
            Map<Integer,BuildingPO> buildingPOMap = new HashMap<>();
            Map<Integer,UserPO> userPOMap = new HashMap<>();
            if (!buildingIds.isEmpty()) {
                buildingPOMap = buildingDAO.selectByIds(buildingIds);
            }
            if (!userIds.isEmpty()) {
                userPOMap = userDAO.selectByIds(userIds);
            }
            for (ConsumerListVO vo : voList) {
                if (buildingPOMap.containsKey(vo.getBuildingId())) {
                    vo.setBuildingName(buildingPOMap.get(vo.getBuildingId()).getName());
                }
                if (userPOMap.containsKey(vo.getUserId())) {
                    vo.setUserName(userPOMap.get(vo.getUserId()).getName());
                }
            }
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void update(String name, Integer buildingId, String floor, String number, String position,
                       byte[] pic, String category, String nature, Integer peopleNum, String linkman,
                       String phone, String operator, BigDecimal expenses, Long expirationDate,
                       String bandwidth, Integer serviceType, String status, String legal, Integer lineNum,
                       String lineType, Long lineOpenDate, String lineStatus, String groupCode, String groupGrade,
                       UserPO currentUser, Integer bindUserId, Integer id) {
        ConsumerPO check = consumerDAO.selectByName(name);
        if (check != null && !check.getId().equals(id)) {
            throw new GisRuntimeException("该名称已经存在");
        }

        ConsumerPO old = consumerDAO.selectById(id);
        ConsumerPO consumerPO = new ConsumerPO();
        consumerPO.setId(id);
        consumerPO.setName(name);
        consumerPO.setBuildingId(buildingId);
        consumerPO.setFloor(floor);
        consumerPO.setNumber(number);
        consumerPO.setPosition(position);
        consumerPO.setPic(pic);
        consumerPO.setCategory(category);
        consumerPO.setNature(nature);
        consumerPO.setPeopleNum(peopleNum);
        consumerPO.setLinkman(linkman);
        consumerPO.setPhone(phone);
        consumerPO.setOperator(operator);
        consumerPO.setExpenses(expenses);
        consumerPO.setExpirationDate(expirationDate);
        consumerPO.setBandwidth(bandwidth);
        consumerPO.setServiceType(serviceType);
        consumerPO.setStatus(status);
        consumerPO.setLegal(legal);
        consumerPO.setLineNum(lineNum);
        consumerPO.setLineType(lineType);
        consumerPO.setLineOpenDate(lineOpenDate);
        consumerPO.setLineStatus(lineStatus);
        consumerPO.setGroupCode(groupCode);
        consumerPO.setGroupGrade(groupGrade);

        consumerPO.setPic(pic == null ? old.getPic() : pic);
        if (isComplete(consumerPO)) {
            consumerPO.setType(ConsumerType.有效建档.getValue());
        }
        else {
            consumerPO.setType(ConsumerType.基础建档.getValue());
        }
        consumerPO.setPic(pic);

        if (currentUser.getRole() == Role.admin.getValue()
                || currentUser.getRole() == Role.headman.getValue()) {
            if (bindUserId != null) {
                consumerPO.setUserId(bindUserId);
            }
            else {
                consumerPO.setUserId(old.getUserId());
            }
        }
        else {
            if (old.getUserId() != 0
                    && !old.getUserId().equals(currentUser.getId())) {
                throw new GisRuntimeException("没有权限修改该数据");
            }
            consumerPO.setUserId(currentUser.getId());
        }

        consumerDAO.update(consumerPO);
    }

    @Override
    public ConsumerVO getById(Integer id) {
        ConsumerPO consumerPO = consumerDAO.selectById(id);
        ConsumerVO vo = new ConsumerVO();
        BeanUtils.copyProperties(consumerPO, vo);
        vo.setTypeName(ConsumerType.getByValue(vo.getType()).getName());
        BuildingPO buildingPO = buildingDAO.selectById(vo.getBuildingId());
        if (vo.getServiceType() != null) {
            vo.setServiceTypeName(ServiceType.getByValue(vo.getServiceType()).getName());
        }
        vo.setBuildingName(buildingPO.getName());
        if (vo.getExpirationDate() != null) {
            vo.setExpirationDateStr(SimpleDateUtil.shortFormat(new Date(vo.getExpirationDate())));
        }
        if (vo.getLineOpenDate() != null) {
            vo.setLineOpenDateStr(SimpleDateUtil.shortFormat(new Date(vo.getLineOpenDate())));
        }
        if (vo.getUserId() != null && vo.getUserId() != 0) {
            UserPO userPO = userDAO.selectById(vo.getUserId());
            vo.setUserName(userPO.getName());
        }
        return vo;
    }

    @Override
    public void delete(Integer id) {
        consumerDAO.deleteById(id);
    }

    /**
     * 是否是基础建档
     * @param consumerPO
     * @return
     */
    private boolean isBasic(ConsumerPO consumerPO) {
        if (consumerPO.getName() == null) {
            return false;
        }
        if (consumerPO.getBuildingId() == null) {
            return false;
        }
        if (consumerPO.getFloor() == null) {
            return false;
        }
        if (consumerPO.getPosition() == null) {
            return false;
        }
        if (consumerPO.getNumber() == null) {
            return false;
        }
        if (consumerPO.getCategory() == null) {
            return false;
        }
        if (consumerPO.getNature() == null) {
            return false;
        }
        if (consumerPO.getPeopleNum() == null) {
            return false;
        }
        if (consumerPO.getPic() == null) {
            return false;
        }
        if (consumerPO.getStatus() == null) {
            return false;
        }
        if (consumerPO.getLegal() == null) {
            return false;
        }
        return true;
    }

    /**
     * 是否是有效建档
     * @param consumerPO
     * @return
     */
    private boolean isComplete(ConsumerPO consumerPO) {
        if (!isBasic(consumerPO)) {
            return false;
        }
        if (consumerPO.getLinkman() == null) {
            return false;
        }
        if (consumerPO.getPhone() == null) {
            return false;
        }
        if (consumerPO.getOperator() == null) {
            return false;
        }
        if (consumerPO.getExpenses() == null) {
            return false;
        }
        if (consumerPO.getExpirationDate() == null) {
            return false;
        }
        if (consumerPO.getBandwidth() == null) {
            return false;
        }
        if (consumerPO.getServiceType() == null) {
            return false;
        }
        else if (consumerPO.getServiceType() == ServiceType.专线产品.getValue()) {
            if (consumerPO.getLineNum() == null) {
                return false;
            }
            if (consumerPO.getLineType() == null) {
                return false;
            }
            if (consumerPO.getLineOpenDate() == null) {
                return false;
            }
            if (consumerPO.getLineStatus() == null) {
                return false;
            }
        }
        if (consumerPO.getGroupCode() == null) {
            return false;
        }
        if (consumerPO.getGroupGrade() == null) {
            return false;
        }
        return true;
    }
}
