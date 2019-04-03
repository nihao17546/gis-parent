package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.ConsumerInfoDAO;
import com.yugoo.gis.dao.GroupDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.ConsumerInfoPO;
import com.yugoo.gis.pojo.po.GroupPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ConsumerInfoListVO;
import com.yugoo.gis.pojo.vo.ListVO;
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
                if (po.getBookedTime() != null && po.getBookedTime() > 0L) {
                    vo.setBookedTimeDate(new Date(po.getBookedTime()));
                }
                if (po.getTransactedTime() != null && po.getTransactedTime() > 0L) {
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
}
