package com.yugoo.gis.user.service.util;

import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao
 * @create 2018/11/10
 **/
@Component
public class AuthUtil {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CenterDAO centerDAO;

    public List<Integer> getCenterIds(Integer userId) {
        UserPO userPO = userDAO.selectById(userId);
        List<Integer> centerIds = null;
        if (userPO.getRole() == Role.admin.getValue()) {

        }
        else if (userPO.getRole() == Role.headman.getValue()) {
            List<CenterPO> centerPOList = centerDAO.selectByGroupId(userPO.getGroupId());
            centerIds = centerPOList.stream().map(centerPO -> {
                return centerPO.getId();
            }).collect(Collectors.toList());
        }
        else {
            centerIds = new ArrayList<>();
            centerIds.add(userPO.getCenterId());
        }
        return centerIds;
    }

    public List<Integer> getCenterIds(Integer userId, String centerName) {
        UserPO userPO = userDAO.selectById(userId);
        List<Integer> centerIds = null;
        if (userPO.getRole() == Role.admin.getValue()) {
            if (centerName != null) {
                centerIds = centerDAO.selectIdByGroupIdAndLikeName(null, null, centerName);
            }
        }
        else if (userPO.getRole() == Role.headman.getValue()) {
            centerIds = centerDAO.selectIdByGroupIdAndLikeName(null, userPO.getGroupId(), centerName);
        }
        else {
            centerIds = centerDAO.selectIdByGroupIdAndLikeName(userPO.getCenterId(), null, centerName);
        }
        return centerIds;
    }

}
