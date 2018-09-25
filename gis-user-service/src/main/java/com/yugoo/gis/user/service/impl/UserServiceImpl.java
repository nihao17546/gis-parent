package com.yugoo.gis.user.service.impl;

import com.google.common.base.Optional;
import com.yugoo.gis.common.constant.Role;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.DesUtils;
import com.yugoo.gis.common.utils.StringUtil;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.UserInfoVO;
import com.yugoo.gis.user.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by nihao on 18/5/8.
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private CenterDAO centerDAO;

    @Override
    public void create(String name, String phone, String password, Integer role, String department,
                       Integer groupId, Integer centerId, String key) {
        checkPhone(null, phone);
        Role roleEnum = Role.getByValue(role);
        if (roleEnum == Role.admin) {
            groupId = 0;
            centerId = 0;
        }
        else if (roleEnum == Role.headman) {
            centerId = 0;
        }
        else {
            CenterPO centerPO = centerDAO.selectById(centerId);
            groupId = centerPO.getGroupId();
        }
        UserPO userPO = new UserPO();
        userPO.setName(name);
        userPO.setPhone(phone);
        if (StringUtil.isEmpty(password)) {
            password = "123456";
        }
        userPO.setPassword(DesUtils.encrypt(password));
        userPO.setRole(role);
        userPO.setDepartment(department);
        userPO.setGroupId(groupId);
        userPO.setGroupId(centerId);
        userPO.setKey(key);
        userDAO.insert(userPO);
    }

    @Override
    public void edit(Integer id, String name, String phone, String password, Integer role, String department, Integer groupId, Integer centerId, String key) {
        checkPhone(id, phone);
        Role roleEnum = Role.getByValue(role);
        if (roleEnum == Role.admin) {
            groupId = 0;
            centerId = 0;
        }
        else if (roleEnum == Role.headman) {
            centerId = 0;
        }
        else {
            CenterPO centerPO = centerDAO.selectById(centerId);
            groupId = centerPO.getGroupId();
        }
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setName(name);
        userPO.setPhone(phone);
        if (StringUtil.isNotEmpty(password)) {
            userPO.setPassword(DesUtils.encrypt(password));
        }
        userPO.setRole(role);
        userPO.setDepartment(department);
        userPO.setGroupId(groupId);
        userPO.setGroupId(centerId);
        userPO.setKey(key);
        userDAO.update(userPO);
    }

    private void checkPhone(Integer id, String phone) {
        UserPO user = userDAO.selectByPhone(phone);
        if (user != null) {
            if (id == null || !id.equals(user.getId())) {
                throw new GisRuntimeException("该手机号已存在");
            }
        }
    }

    @Override
    public void edit(Integer id, String name, String phone, String department) {
        checkPhone(id, phone);
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setName(name);
        userPO.setPhone(phone);
        userPO.setDepartment(department);
        userDAO.update(userPO);
    }

    @Override
    public void edit(Integer id, String password) {
        UserPO userPO = new UserPO();
        userPO.setId(id);
        userPO.setPassword(DesUtils.encrypt(password));
        userDAO.update(userPO);
    }

    @Override
    public UserInfoVO getById(Integer id) {
        UserPO userPO = userDAO.selectById(id);
        UserInfoVO infoVO = new UserInfoVO();
        BeanUtils.copyProperties(userPO, infoVO);
        return infoVO;
    }

    @Override
    public Optional<String> login(String phone, String password, String key) {
        UserPO userPO = userDAO.selectByPhone(phone);
        if(userPO != null){
            if (key != null && !key.equals(userPO.getKey())) {
                throw new GisRuntimeException("手机序列号不一致");
            }
            if(DesUtils.encrypt(password).equals(userPO.getPassword())){
                return Optional.of(System.currentTimeMillis() + "&_" + userPO.getId() + "&_" + userPO.getRole());
            }
        }
        return Optional.absent();
    }
}
