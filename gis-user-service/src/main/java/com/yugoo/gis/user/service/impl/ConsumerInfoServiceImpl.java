package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.common.utils.SimpleDateUtil;
import com.yugoo.gis.common.utils.ZipUtils;
import com.yugoo.gis.dao.ConsumerInfoDAO;
import com.yugoo.gis.dao.GroupDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.ConsumerInfoPO;
import com.yugoo.gis.pojo.po.GroupPO;
import com.yugoo.gis.pojo.po.StatisticCustomerPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ConsumerInfoListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StatisticCustomerVO;
import com.yugoo.gis.user.service.IConsumerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsumerInfoServiceImpl implements IConsumerInfoService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConsumerInfoDAO consumerInfoDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GroupDAO groupDAO;

    @Override
    public ListVO<ConsumerInfoListVO> list(Integer curPage, Integer pageSize, Date startTime, Date endTime) {
        Long count = consumerInfoDAO.selectCount(startTime, endTime);
        ListVO<ConsumerInfoListVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<ConsumerInfoPO> poList = consumerInfoDAO.select(startTime, endTime, (curPage - 1) * pageSize, pageSize);
            List<Integer> userIds = new ArrayList<>();
            List<ConsumerInfoListVO> voList = poList.stream().map(po -> {
                ConsumerInfoListVO vo = new ConsumerInfoListVO();
                BeanUtils.copyProperties(po, vo);
                if (po.getStatus() == 1) {
                    vo.setStatusStr("预约");
                } else if (po.getStatus() == 2) {
                    vo.setStatusStr("已签约");
                }
                if (po.getBookedTime() != null && po.getBookedTime() != 0L) {
                    vo.setBookedTimeDate(new Date(po.getBookedTime()));
                }
                if (po.getTransactedTime() != null && po.getTransactedTime() != 0L) {
                    vo.setTransactedTimeDate(new Date(po.getTransactedTime()));
                }
                if (po.getUserId() != null && !userIds.contains(po.getUserId())) {
                    userIds.add(po.getUserId());
                }
                return vo;
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(userIds)) {
                Map<Integer,UserPO> userPOMap = userDAO.selectByIds(userIds);
                Map<Integer,Integer> userId2GroupId = new HashMap<>();
                if (!CollectionUtils.isEmpty(userPOMap)) {
                    List<Integer> groupIds = new ArrayList<>();
                    for (Integer userId : userPOMap.keySet()) {
                        Integer groupId = userPOMap.get(userId).getGroupId();
                        if (groupId != null) {
                            if (!groupIds.contains(groupId)) {
                                groupIds.add(groupId);
                            }
                            userId2GroupId.put(userId, groupId);
                        }
                    }
                    if (!CollectionUtils.isEmpty(groupIds)) {
                        Map<Integer,GroupPO> groupPOMap = groupDAO.selectByIds(groupIds);
                        if (!CollectionUtils.isEmpty(groupPOMap)) {
                            for (ConsumerInfoListVO vo : voList) {
                                Integer groupId = userId2GroupId.get(vo.getUserId());
                                if (groupId != null) {
                                    GroupPO groupPO = groupPOMap.get(groupId);
                                    if (groupPO != null) {
                                        vo.setGroupName(groupPO.getName());
                                    }
                                }
                            }
                        }
                    }
                    for (ConsumerInfoListVO vo : voList) {
                        UserPO userPO = userPOMap.get(vo.getUserId());
                        if (userPO != null) {
                            vo.setUserName(userPO.getName());
                        }
                    }
                }
            }
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void delete(Integer id) {
        consumerInfoDAO.deleteById(id);
    }

    @Override
    public List<StatisticCustomerVO> statistic(Date startTime, Date endTime) {
        String today = SimpleDateUtil.shortFormat(new Date());
        Date todayStart = SimpleDateUtil.parse(today + " 00:00:00");
        Date todayEnd = SimpleDateUtil.parse(today + " 23:59:59");
        Map<Integer,StatisticCustomerPO> todayTotal = consumerInfoDAO.selectByCtime(todayStart, todayEnd);
        Map<Integer,StatisticCustomerPO> todayBooked = consumerInfoDAO.selectByBookedTime(todayStart.getTime(), todayEnd.getTime());
        Map<Integer,StatisticCustomerPO> todayTransacted = consumerInfoDAO.selectByTransactedTime(todayStart.getTime(), todayEnd.getTime());
        Map<Integer,StatisticCustomerPO> accumulateBooked = consumerInfoDAO.selectByBookedTime(startTime.getTime(), endTime.getTime());
        Map<Integer,StatisticCustomerPO> accumulateTransacted = consumerInfoDAO.selectByTransactedTime(startTime.getTime(), endTime.getTime());
        List<StatisticCustomerVO> list = new ArrayList<>();
        Map<Integer,StatisticCustomerVO> cachMap = new HashMap<>();
        for (Integer userId : todayTotal.keySet()) {
            StatisticCustomerVO vo = new StatisticCustomerVO();
            vo.setUserId(userId);
            vo.setTodayTotalCount(todayTotal.get(userId).getCon());
            list.add(vo);
            cachMap.put(userId, vo);
        }
        for (Integer userId : todayBooked.keySet()) {
            if (!cachMap.containsKey(userId)) {
                StatisticCustomerVO vo = new StatisticCustomerVO();
                vo.setUserId(userId);
                vo.setTodayBookedCount(todayBooked.get(userId).getCon());
                list.add(vo);
                cachMap.put(userId, vo);
            } else {
                StatisticCustomerVO vo = cachMap.get(userId);
                vo.setTodayBookedCount(todayBooked.get(userId).getCon());
            }
        }
        for (Integer userId : todayTransacted.keySet()) {
            if (!cachMap.containsKey(userId)) {
                StatisticCustomerVO vo = new StatisticCustomerVO();
                vo.setUserId(userId);
                vo.setTodayTransactedCount(todayTransacted.get(userId).getCon());
                list.add(vo);
                cachMap.put(userId, vo);
            } else {
                StatisticCustomerVO vo = cachMap.get(userId);
                vo.setTodayTransactedCount(todayTransacted.get(userId).getCon());
            }
        }
        for (Integer userId : accumulateBooked.keySet()) {
            if (!cachMap.containsKey(userId)) {
                StatisticCustomerVO vo = new StatisticCustomerVO();
                vo.setUserId(userId);
                vo.setAccumulateBookedCount(accumulateBooked.get(userId).getCon());
                list.add(vo);
                cachMap.put(userId, vo);
            } else {
                StatisticCustomerVO vo = cachMap.get(userId);
                vo.setAccumulateBookedCount(accumulateBooked.get(userId).getCon());
            }
        }
        for (Integer userId : accumulateTransacted.keySet()) {
            if (!cachMap.containsKey(userId)) {
                StatisticCustomerVO vo = new StatisticCustomerVO();
                vo.setUserId(userId);
                vo.setAccumulateTransactedCount(accumulateTransacted.get(userId).getCon());
                list.add(vo);
                cachMap.put(userId, vo);
            } else {
                StatisticCustomerVO vo = cachMap.get(userId);
                vo.setAccumulateTransactedCount(accumulateTransacted.get(userId).getCon());
            }
        }
        List<Integer> userIds = new ArrayList<>(cachMap.keySet());
        if (!CollectionUtils.isEmpty(userIds)) {
            Map<Integer,UserPO> userPOMap = userDAO.selectByIds(userIds);
            Map<Integer,Integer> userId2GroupId = new HashMap<>();
            if (!CollectionUtils.isEmpty(userPOMap)) {
                List<Integer> groupIds = new ArrayList<>();
                for (Integer userId : userPOMap.keySet()) {
                    Integer groupId = userPOMap.get(userId).getGroupId();
                    if (groupId != null) {
                        userId2GroupId.put(userId, groupId);
                        if (!groupIds.contains(groupId)) {
                            groupIds.add(groupId);
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(groupIds)) {
                    Map<Integer,GroupPO> groupPOMap = groupDAO.selectByIds(groupIds);
                    if (!CollectionUtils.isEmpty(groupPOMap)) {
                        for (StatisticCustomerVO vo : list) {
                            Integer groupId = userId2GroupId.get(vo.getUserId());
                            if (groupId != null) {
                                GroupPO groupPO = groupPOMap.get(groupId);
                                if (groupPO != null) {
                                    vo.setGroupName(groupPO.getName());
                                }
                            }
                        }
                    }
                }
                for (StatisticCustomerVO vo : list) {
                    UserPO userPO = userPOMap.get(vo.getUserId());
                    if (userPO != null) {
                        vo.setUserName(userPO.getName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<ZipUtils.BufferFile> getBufferFiles(List<Integer> ids) {
        List<ConsumerInfoPO> list = consumerInfoDAO.selectByIds(ids);
        List<ZipUtils.BufferFile> bufferFileList = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        Map<Integer,ZipUtils.BufferFile> cacheMap = new HashMap<>();
        Map<Integer,Integer> id2UserId = new HashMap<>();
        for (ConsumerInfoPO po : list) {
            if (po.getPhoto() != null && po.getPhoto().length > 0) {
                ZipUtils.BufferFile bufferFile = new ZipUtils.BufferFile();
                bufferFile.data = po.getPhoto();
                bufferFile.name = SimpleDateUtil.format(po.getCtime()) + ".png";
                bufferFileList.add(bufferFile);
                if (!userIds.contains(po.getUserId())) {
                    userIds.add(po.getUserId());
                }
                cacheMap.put(po.getId(), bufferFile);
                id2UserId.put(po.getId(), po.getUserId());
            }
        }
        if (!CollectionUtils.isEmpty(userIds)) {
            Map<Integer,UserPO> userPOMap = userDAO.selectByIds(userIds);
            if (!CollectionUtils.isEmpty(userPOMap)) {
                for (Integer id : cacheMap.keySet()) {
                    UserPO userPO = userPOMap.get(id2UserId.get(id));
                    if (userPO != null) {
                        cacheMap.get(id).name = userPO.getName() + "_" + cacheMap.get(id).name;
                    }
                }
            }
        }
        return bufferFileList;
    }
}
