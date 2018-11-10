package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.constant.ConsumerType;
import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.common.constant.ServiceType;
import com.yugoo.gis.common.utils.SimpleDateUtil;
import com.yugoo.gis.dao.*;
import com.yugoo.gis.pojo.po.*;
import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StatisticCenterVO;
import com.yugoo.gis.pojo.vo.StatisticUserVO;
import com.yugoo.gis.user.service.IStatisticService;
import com.yugoo.gis.user.service.util.AuthUtil;
import com.yugoo.gis.user.service.util.MapUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/11/5
 */
@Service
public class StatisticServiceImpl implements IStatisticService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private ConsumerDAO consumerDAO;
    @Autowired
    private ResourceDAO resourceDAO;
    @Autowired
    private StatisticDAO statisticDAO;
    @Autowired
    private ConfigDAO configDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AuthUtil authUtil;

    private DecimalFormat decimalFormat = new DecimalFormat("#.##%");

    @Override
    public List<StatisticCenterVO> listCenter(Integer curPage, Integer pageSize, String centerName, String sortColumn, String order, Integer currentUserId) {
        List<Integer> currentCenterIds = authUtil.getCenterIds(currentUserId, centerName);
        if (currentCenterIds != null && currentCenterIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<StatisticCenterPO> poList = statisticDAO.selectCenter(currentCenterIds, sortColumn, order);
        List<Integer> centerIds = new ArrayList<>();
        List<StatisticCenterVO> voList = poList.stream().map(po -> {
            StatisticCenterVO vo = new StatisticCenterVO();
            BeanUtils.copyProperties(po, vo);
            centerIds.add(po.getCenterId());
            vo.setSpecialLineRatioStr(decimalFormat.format(vo.getSpecialLineRatio()));
            vo.setHotelRatioStr(decimalFormat.format(vo.getHotelRatio()));
            vo.setBusinessRatioStr(decimalFormat.format(vo.getBusinessRatio()));
            return vo;
        }).collect(Collectors.toList());
        if (!centerIds.isEmpty()) {
            Map<Integer,CenterPO> centerPOMap = centerDAO.selectByIds(centerIds);
            if (centerPOMap != null && !centerPOMap.isEmpty()) {
                for (StatisticCenterVO vo : voList) {
                    CenterPO centerPO = centerPOMap.get(vo.getCenterId());
                    if (centerPO != null) {
                        vo.setCenterName(centerPO.getName());
                        vo.setDistrict(centerPO.getDistrict());
                    }
                }
            }
        }
        return voList;
    }

    @Transactional
    @Override
    public void statisticCenter() {
        logger.info("营销中心统计");
        long start = System.currentTimeMillis();
        List<CenterPO> centerPOList = centerDAO.select(null, null, new RowBounds(0, Integer.MAX_VALUE));
        if (!centerPOList.isEmpty()) {
            List<StatisticCenterPO> list = centerPOList.stream().map(center -> {
                StatisticCenterPO po = new StatisticCenterPO();
                po.setCenterId(center.getId());
                po.setNotArchiveCount(0);
                po.setBasicArchiveCount(0);
                po.setEffectiveArchiveCount(0);
                List<ConsumerPO> consumerPOList = consumerDAO.selectByCenter(center.getLoMin(), center.getLoMax(),
                        center.getLaMin(), center.getLaMax());
                List<List<Double>> lists = JSON.parseObject(center.getRegion(), new TypeReference<List<List<Double>>>(){});
                int special = 0, hotel = 0, business = 0, total = 0;
                for (ConsumerPO consumerPO : consumerPOList) {
                    if (MapUtil.isPtInPoly(consumerPO.getLongitude(), consumerPO.getLatitude(), lists)) {
                        total ++;
                        if (consumerPO.getType() == ConsumerType.未建档.getValue()) {
                            po.setNotArchiveCount(po.getNotArchiveCount() + 1);
                        }
                        else if (consumerPO.getType() == ConsumerType.基础建档.getValue()) {
                            po.setBasicArchiveCount(po.getBasicArchiveCount() + 1);
                        }
                        else if (consumerPO.getType() == ConsumerType.有效建档.getValue()) {
                            po.setEffectiveArchiveCount(po.getEffectiveArchiveCount() + 1);
                        }
                        if (consumerPO.getServiceType() == ServiceType.专线产品.getValue()) {
                            special ++;
                        }
                        else if (consumerPO.getServiceType() == ServiceType.酒店产品.getValue()) {
                            hotel ++;
                        }
                        else if (consumerPO.getServiceType() == ServiceType.商务动力.getValue()) {
                            business ++;
                        }
                    }
                }
                if (total > 0) {
                    po.setSpecialLineRatio(new BigDecimal((float) special / total)
                            .setScale(4, BigDecimal.ROUND_HALF_UP).floatValue());
                    po.setHotelRatio(new BigDecimal((float) hotel / total)
                            .setScale(4, BigDecimal.ROUND_HALF_UP).floatValue());
                    po.setBusinessRatio(new BigDecimal((float) business / total)
                            .setScale(4, BigDecimal.ROUND_HALF_UP).floatValue());
                }
                else {
                    po.setSpecialLineRatio(0.0000F);
                    po.setHotelRatio(0.0000F);
                    po.setBusinessRatio(0.0000F);
                }

                po.setWholePortCount(0);
                po.setUsedPortCount(0);
                List<ResourcePO> resourcePOList = resourceDAO.selectByCenter(center.getLoMin(), center.getLoMax(),
                        center.getLaMin(), center.getLaMax());
                for (ResourcePO resourcePO : resourcePOList) {
                    if (MapUtil.isPtInPoly(resourcePO.getLongitude(), resourcePO.getLatitude(), lists)) {
                        po.setWholePortCount(po.getWholePortCount() + resourcePO.getAllPortCount());
                        po.setUsedPortCount(po.getUsedPortCount() + (resourcePO.getAllPortCount() - resourcePO.getIdelPortCount()));
                    }
                }

                return po;
            }).collect(Collectors.toList());

            int delete = statisticDAO.deleteCenter();
            int insert = statisticDAO.batchInsertCenter(list);
            logger.info("删除营销中心统计数据{}条", delete);
            logger.info("新增营销中心统计数据{}条", insert);
            logger.info("营销中心统计完成,消耗{}毫秒", System.currentTimeMillis() - start);
        }
    }

    @Transactional
    @Override
    public void statisticUser() {
        logger.info("客户经理中心统计");
        long start = System.currentTimeMillis();
        List<UserPO> userPOList = userDAO.selectByNameAndRole(null, Role.member.getValue());
        List<Integer> userIds = new ArrayList<>();
        List<StatisticUserPO> statisticUserPOList = userPOList.stream().map(userPO -> {
            userIds.add(userPO.getId());
            StatisticUserPO statisticUserPO = new StatisticUserPO();
            statisticUserPO.setUserId(userPO.getId());
            statisticUserPO.setSpecialLineCount(0);
            statisticUserPO.setBasicArchiveCount(0);
            statisticUserPO.setEffectiveArchiveCount(0);
            return statisticUserPO;
        }).collect(Collectors.toList());
        if (!userIds.isEmpty()) {
            Map<Integer,StatisticUserPO> statisticUserPOMap = statisticDAO.selectStatisticUser(userIds);
            for (StatisticUserPO statisticUserPO : statisticUserPOList) {
                StatisticUserPO po = statisticUserPOMap.get(statisticUserPO.getUserId());
                if (po != null) {
                    statisticUserPO.setSpecialLineCount(po.getSpecialLineCount());
                    statisticUserPO.setBasicArchiveCount(po.getBasicArchiveCount());
                    statisticUserPO.setEffectiveArchiveCount(po.getEffectiveArchiveCount());
                }
            }
        }
        if (!statisticUserPOList.isEmpty()) {
            int delete = statisticDAO.deleteUser();
            int insert = statisticDAO.batchInsertUser(statisticUserPOList);
            logger.info("删除客户经理统计数据{}条", delete);
            logger.info("新增客户经理统计数据{}条", insert);
        }
        logger.info("客户经理统计完成,消耗{}毫秒", System.currentTimeMillis() - start);
    }

    @Override
    public ListVO<ConsumerListVO> listConsumer(Integer curPage, Integer pageSize, String consumerName, String order, Integer currentUserId) {
        UserPO currentUser = userDAO.selectById(currentUserId);
        List<Integer> userIds = null;
        if (currentUser.getRole() == Role.admin.getValue()) {

        }
        else if (currentUser.getRole() == Role.headman.getValue()) {
            List<UserPO> userPOList = userDAO.selectSubordinates(currentUser.getGroupId(), null);
            userIds = userPOList.stream().map(userPO -> {
                return userPO.getId();
            }).collect(Collectors.toList());
        }
        else {
            userIds = new ArrayList<>();
            userIds.add(currentUserId);
        }

        ConfigPO configPO = configDAO.select();
        long expirationDate = configPO.getExpirationDateLimit() * 24 * 60 * 60 * 1000;
        Date now = new Date();
        long current = SimpleDateUtil.parse(SimpleDateUtil.shortFormat(now) + " 00:00:00").getTime();
        ListVO<ConsumerListVO> listVO = new ListVO<>(curPage, pageSize);
        long count = statisticDAO.selectConsumerCount(current + expirationDate, userIds, consumerName, order);
        if (count > 0) {
            List<ConsumerPO> consumerPOList = statisticDAO.selectConsumer(current + expirationDate, userIds, consumerName, order,
                    new RowBounds((curPage - 1) * pageSize, pageSize));
            List<ConsumerListVO> voList = consumerPOList.stream().map(po -> {
                ConsumerListVO vo = new ConsumerListVO();
                BeanUtils.copyProperties(po, vo);
                if (po.getExpirationDate() != null) {
                    vo.setExpirationDateStr(SimpleDateUtil.shortFormat(new Date(po.getExpirationDate())));
                }
                return vo;
            }).collect(Collectors.toList());
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public List<StatisticUserVO> listUser(String centerName, String sortColumn, String order, Integer currentUserId) {
        List<Integer> currentCenterIds = authUtil.getCenterIds(currentUserId, centerName);

        List<Integer> paramUserIds = null;
        if (currentCenterIds != null) {
            if (currentCenterIds.isEmpty()) {
                paramUserIds = new ArrayList<>();
            }
            else {
                paramUserIds = userDAO.selectIdByCenterIdsAndRole(currentCenterIds, Role.member.getValue());
            }
        }

        if (paramUserIds != null && paramUserIds.isEmpty()) {
            return new ArrayList<>();
        }
        else {
            List<StatisticUserPO> poList = statisticDAO.selectUser(paramUserIds, sortColumn, order);
            List<Integer> userIds = new ArrayList<>();
            List<StatisticUserVO> voList = poList.stream().map(po -> {
                userIds.add(po.getUserId());
                StatisticUserVO vo = new StatisticUserVO();
                BeanUtils.copyProperties(po, vo);
                return  vo;
            }).collect(Collectors.toList());
            if (!userIds.isEmpty()) {
                Map<Integer,UserPO> userPOMap = userDAO.selectByIds(userIds);
                if (!userPOMap.isEmpty()) {
                    List<Integer> centerIds = new ArrayList<>();
                    for (StatisticUserVO vo : voList) {
                        UserPO userPO = userPOMap.get(vo.getUserId());
                        if (userPO != null) {
                            vo.setUserName(userPO.getName());
                            vo.setCenterId(userPO.getCenterId());
                            if (userPO.getCenterId() != null
                                    && userPO.getCenterId() != 0
                                    && !centerIds.contains(userPO.getCenterId())) {
                                centerIds.add(userPO.getCenterId());
                            }
                        }
                    }
                    if (!centerIds.isEmpty()) {
                        Map<Integer,CenterPO> centerPOMap = centerDAO.selectByIds(centerIds);
                        if (!centerPOMap.isEmpty()) {
                            for (StatisticUserVO vo : voList) {
                                CenterPO centerPO = centerPOMap.get(vo.getCenterId());
                                if (centerPO != null) {
                                    vo.setCenterName(centerPO.getName());
                                }
                            }
                        }
                    }
                }
            }
            return voList;
        }
    }
}
