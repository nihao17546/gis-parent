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
import com.yugoo.gis.pojo.excel.ConsumerImport;
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
import org.springframework.transaction.annotation.Transactional;

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
    public void create(String name, Integer buildingId, Integer floor, String number, String position, byte[] pic,
                       String category, String nature, Integer peopleNum, String linkman, String phone, String operator,
                       BigDecimal expenses, Long expirationDate, String bandwidth, Integer serviceType, String status,
                       String legal, Integer lineNum, String lineType, Long lineOpenDate, String lineStatus, String groupCode,
                       String groupGrade, UserPO currentUser, Integer bindUserId, Double longitude, Double latitude,
                       String expensesName, Long orderTime, String memberRole, String memberRoleRealNum, String memberExpensesName) {
        ConsumerPO check = consumerDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        buildingId = buildingId == null ? 0 : buildingId;
        if (buildingId == 0) {
            if (longitude == null || latitude == null) {
                throw new GisRuntimeException("信息不完整，缺少经纬度坐标或未关联建筑");
            }
        }
        else {
            BuildingPO buildingPO = buildingDAO.selectById(buildingId);
            longitude = buildingPO.getLongitude();
            latitude = buildingPO.getLatitude();
        }
        ConsumerPO consumerPO = new ConsumerPO();
        consumerPO.setLongitude(longitude);
        consumerPO.setLatitude(latitude);
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
        consumerPO.setExpensesName(expensesName);
        consumerPO.setOrderTime(orderTime);
        consumerPO.setMemberRole(memberRole);
        consumerPO.setMemberRoleRealNum(memberRoleRealNum);
        consumerPO.setMemberExpensesName(memberExpensesName);

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

    private Map<String,List<Integer>> auth(Integer buildingId, UserPO currentUser) {
        Map<String,List<Integer>> map = new HashMap<>();
        List<Integer> buildingIdsParam = new ArrayList<>();
        List<Integer> userIdsParam = new ArrayList<>();
        if (currentUser.getRole() == Role.admin.getValue()) {
            if (buildingId != null && buildingId != 0) {
                buildingIdsParam.add(buildingId);
            }
            else {
                buildingIdsParam = null;
            }
        }
        else {
            currentUser = userDAO.selectById(currentUser.getId());
            if (currentUser.getRole() == Role.headman.getValue()) {
                List<CenterPO> centerPOList = centerDAO.selectByGroupId(currentUser.getGroupId());
                for (CenterPO centerPO : centerPOList) {
                    List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
                    List<BuildingPO> buildingPOList = buildingDAO.selectAllByLoAndLa(
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
                userIdsParam.add(0);
                userIdsParam.add(currentUser.getId());
                CenterPO centerPO = centerDAO.selectById(currentUser.getCenterId());
                List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
                List<BuildingPO> buildingPOList = buildingDAO.selectAllByLoAndLa(
                        centerPO.getLoMin(), centerPO.getLoMax(), centerPO.getLaMin(), centerPO.getLaMax());
                for (BuildingPO buildingPO : buildingPOList) {
                    if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)
                            && !buildingIdsParam.contains(buildingPO.getId())) {
                        buildingIdsParam.add(buildingPO.getId());
                    }
                }
            }
            if (buildingId != null && buildingId != 0) {
                if (buildingIdsParam.contains(buildingId)) {
                    buildingIdsParam = new ArrayList<>();
                    buildingIdsParam.add(buildingId);
                }
                else {
                    buildingIdsParam = new ArrayList<>();
                }
            }
            else {
                buildingIdsParam.add(0);
            }
        }
        map.put("building", buildingIdsParam);
        map.put("user", userIdsParam);
        return map;
    }

    @Override
    public ListVO<ConsumerListVO> list(Integer curPage, Integer pageSize, String name, UserPO currentUser, Integer buildingId) {
        Map<String,List<Integer>> map = auth(buildingId, currentUser);
        Object building = map.get("building");
        Object user = map.get("user");
        List<Integer> buildingIdsParam = building == null ? null : (List<Integer>) building;
        List<Integer> userIdsParam = user == null ? null : (List<Integer>) user;

        if (buildingIdsParam != null && buildingIdsParam.isEmpty()) {
            return new ListVO<>(curPage, pageSize);
        }

        long count = consumerDAO.selectCount(name, userIdsParam, buildingIdsParam);
        ListVO<ConsumerListVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<ConsumerPO> poList = consumerDAO.select(name, userIdsParam, buildingIdsParam,
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
                if (po.getExpirationDate() != null && po.getExpirationDate() != 0) {
                    vo.setExpirationDateStr(SimpleDateUtil.shortFormat(new Date(po.getExpirationDate())));
                }
                if (po.getLineOpenDate() != null && po.getLineOpenDate() != 0) {
                    vo.setLineOpenDateStr(SimpleDateUtil.shortFormat(new Date(po.getLineOpenDate())));
                }
                if (po.getOrderTime() != null && po.getOrderTime() != 0) {
                    vo.setOrderTimeStr(SimpleDateUtil.shortFormat(new Date(po.getOrderTime())));
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
                    BuildingPO buildingPO = buildingPOMap.get(vo.getBuildingId());
                    vo.setBuildingName(buildingPO.getName());
                    vo.setLongitude(buildingPO.getLongitude());
                    vo.setLatitude(buildingPO.getLatitude());
                }
                if (userPOMap.containsKey(vo.getUserId())) {
                    vo.setUserName(userPOMap.get(vo.getUserId()).getName());
                    vo.setUserNumber(userPOMap.get(vo.getUserId()).getNumber());
                }
            }
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void update(String name, Integer buildingId, Integer floor, String number, String position,
                       byte[] pic, String category, String nature, Integer peopleNum, String linkman,
                       String phone, String operator, BigDecimal expenses, Long expirationDate,
                       String bandwidth, Integer serviceType, String status, String legal, Integer lineNum,
                       String lineType, Long lineOpenDate, String lineStatus, String groupCode, String groupGrade,
                       UserPO currentUser, Integer bindUserId, Integer id,
                       String expensesName, Long orderTime, String memberRole, String memberRoleRealNum, String memberExpensesName) {
        ConsumerPO check = consumerDAO.selectByName(name);
        if (check != null && !check.getId().equals(id)) {
            throw new GisRuntimeException("该名称已经存在");
        }
        if (buildingId == null || buildingId == 0) {
            throw new GisRuntimeException("信息不完整，未关联建筑");
        }
        BuildingPO buildingPO = buildingDAO.selectById(buildingId);
        ConsumerPO old = consumerDAO.selectById(id);
        ConsumerPO consumerPO = new ConsumerPO();
        consumerPO.setLongitude(buildingPO.getLongitude());
        consumerPO.setLatitude(buildingPO.getLatitude());
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
        consumerPO.setExpensesName(expensesName);
        consumerPO.setOrderTime(orderTime);
        consumerPO.setMemberRole(memberRole);
        consumerPO.setMemberRoleRealNum(memberRoleRealNum);
        consumerPO.setMemberExpensesName(memberExpensesName);

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
        if (vo.getServiceType() != null) {
            vo.setServiceTypeName(ServiceType.getByValue(vo.getServiceType()).getName());
        }
        if (vo.getBuildingId() != null && vo.getBuildingId() != 0) {
            BuildingPO buildingPO = buildingDAO.selectById(vo.getBuildingId());
            vo.setBuildingName(buildingPO.getName());
            vo.setLongitude(buildingPO.getLongitude());
            vo.setLatitude(buildingPO.getLatitude());
        }
        if (vo.getExpirationDate() != null && vo.getExpirationDate() != 0) {
            vo.setExpirationDateStr(SimpleDateUtil.shortFormat(new Date(vo.getExpirationDate())));
        }
        if (vo.getLineOpenDate() != null && vo.getLineOpenDate() != 0) {
            vo.setLineOpenDateStr(SimpleDateUtil.shortFormat(new Date(vo.getLineOpenDate())));
        }
        if (vo.getOrderTime() != null && vo.getOrderTime() != 0) {
            vo.setOrderTimeStr(SimpleDateUtil.shortFormat(new Date(vo.getOrderTime())));
        }
        if (vo.getUserId() != null && vo.getUserId() != 0) {
            UserPO userPO = userDAO.selectById(vo.getUserId());
            vo.setUserName(userPO.getName());
            vo.setUserNumber(userPO.getNumber());
        }
        return vo;
    }

    @Override
    public void delete(Integer id) {
        consumerDAO.deleteById(id);
    }

    @Override
    public List<ConsumerVO> searchFromMap(String name, Double loMin, Double loMax, Double laMin, Double laMax, UserPO currentUser) {
        Map<String,List<Integer>> map = auth(null, currentUser);
        Object building = map.get("building");
        Object user = map.get("user");
        List<Integer> buildingIdsParam = building == null ? null : (List<Integer>) building;
        List<Integer> userIdsParam = user == null ? null : (List<Integer>) user;

        List<ConsumerPO> consumerPOList = consumerDAO.selectFromMap(name, userIdsParam, loMin, loMax, laMin, laMax, buildingIdsParam);
        List<ConsumerVO> consumerVOList = consumerPOList.stream().map(po -> {
            ConsumerVO vo = new ConsumerVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return consumerVOList;
    }

    @Transactional
    @Override
    public String importData(List<ConsumerImport> list) {
        List<ConsumerPO> consumerPOList = new ArrayList<>();
        for (ConsumerImport consumerImport : list) {
            complete(consumerImport);
            ConsumerPO consumerPO = new ConsumerPO();
            BeanUtils.copyProperties(consumerImport, consumerPO);
            if (isComplete(consumerPO)) {
                consumerPO.setType(ConsumerType.有效建档.getValue());
            }
            else if (isBasic(consumerPO)){
                consumerPO.setType(ConsumerType.基础建档.getValue());
            }
            else {
                consumerPO.setType(ConsumerType.未建档.getValue());
            }
            consumerPOList.add(consumerPO);
        }
        int re = consumerDAO.batchInsert(consumerPOList);
        return "总共导入客户" + consumerPOList.size() + "条数据(新增" + (2 * consumerPOList.size() - re)
                + " 条,更新" + (re - consumerPOList.size())+ "条)";
    }

    private void complete(ConsumerImport consumerImport) {
        if (consumerImport.getBuildingName() != null && !consumerImport.getBuildingName().equals("")) {
            BuildingPO buildingPO = buildingDAO.selectByName(consumerImport.getBuildingName());
            if (buildingPO == null) {
                throw new GisRuntimeException(consumerImport.getR() + "【建筑】[" + consumerImport.getBuildingName() + "]不存在");
            }
            consumerImport.setBuildingId(buildingPO.getId());
            consumerImport.setLongitude(buildingPO.getLongitude());
            consumerImport.setLatitude(buildingPO.getLatitude());
        }
        else {
            throw new GisRuntimeException(consumerImport.getR() + "【建筑】未填写");
        }
        if (consumerImport.getName() == null || consumerImport.getName().equals("")) {
            throw new GisRuntimeException(consumerImport.getR() + "【集团名称】未填写");
        }
        consumerImport.setExpirationDate(getDate(consumerImport.getExpirationDateStr(),
                consumerImport.getR() + "【业务到期时间】解析错误，格式要求yyyyMMdd或yyyy-MM-dd"));
        consumerImport.setLineOpenDate(getDate(consumerImport.getLineOpenDateStr(),
                consumerImport.getR() + "【专线开户时间】解析错误，格式要求yyyyMMdd或yyyy-MM-dd"));
        consumerImport.setOrderTime(getDate(consumerImport.getOrderTimeStr(),
                consumerImport.getR() + "【订购时间】解析错误，格式要求yyyyMMdd或yyyy-MM-dd"));
        if (consumerImport.getServiceTypeStr() != null && !consumerImport.getServiceTypeStr().equals("")) {
            try {
                ServiceType serviceType = ServiceType.getByName(consumerImport.getServiceTypeStr());
                consumerImport.setServiceType(serviceType.getValue());
            } catch (RuntimeException e) {
                throw new GisRuntimeException(consumerImport.getR() + "【业务类型】解析错误，仅支持'专线产品,酒店产品,商务动力'");
            }
        }
        if (consumerImport.getUserNumber() != null && !consumerImport.getUserNumber().equals("")) {
            UserPO userPO = userDAO.selectByNumber(consumerImport.getUserNumber());
            if (userPO == null) {
                throw new GisRuntimeException(consumerImport.getR() + "【客户经理工号】解析错误，不存在该客户经理");
            }
            else if (userPO.getRole() != Role.member.getValue()) {
                throw new GisRuntimeException(consumerImport.getR() + "【客户经理工号】解析错误，不是客户经理");
            }
            consumerImport.setUserId(userPO.getId());
        }
        else {
            consumerImport.setUserId(0);
        }
        if (consumerImport.getFloor() == null) {
            consumerImport.setFloor(1);
        }
    }

    private Long getDate(String str, String name) {
        if (str != null && !str.equals("")) {
            try {
                return SimpleDateUtil.shortParseSo(str).getTime();
            } catch (Exception e) {
                try {
                    return SimpleDateUtil.shortParse(str).getTime();
                } catch (Exception e1) {
                    throw new GisRuntimeException(name + "");
                }
            }
        }
        return 0L;
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
//        if (consumerPO.getPic() == null) {
//            return false;
//        }
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
        if (consumerPO.getExpirationDate() == null || consumerPO.getExpirationDate() == 0) {
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
            if (consumerPO.getLineOpenDate() == null || consumerPO.getLineOpenDate() == 0) {
                return false;
            }
            if (consumerPO.getLineStatus() == null) {
                return false;
            }
        }
        else {
            if (consumerPO.getExpensesName() == null) {
                return false;
            }
            if (consumerPO.getOrderTime() == null || consumerPO.getOrderTime() == 0) {
                return false;
            }
            if (consumerPO.getMemberRole() == null) {
                return false;
            }
            if (consumerPO.getMemberRoleRealNum() == null) {
                return false;
            }
            if (consumerPO.getMemberExpensesName() == null) {
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
