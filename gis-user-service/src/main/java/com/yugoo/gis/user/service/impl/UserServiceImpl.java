package com.yugoo.gis.user.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.DesUtils;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.user.service.IUserService;
import com.yugoo.gis.user.service.cache.CacheManager;
import com.yugoo.gis.user.service.cache.CacheManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by nihao on 18/5/8.
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserDAO userDAO;

    private CacheManager<Integer,UserPO> userCache = CacheManager.build(1l, TimeUnit.HOURS);
    {
        CacheManagerUtils.add("user" ,userCache);
    }

    @Override
    public UserPO getUserById(Integer userId) {
        UserPO userPO = userCache.getIfPresent(userId);
        if(userPO == null){
            userPO = userDAO.selectById(userId);
            Preconditions.checkNotNull(userPO);
            userCache.set(userId, userPO);
        }
        return userPO;
    }

    @Override
    public Optional<String> login(String phone, String password) {
        UserPO userPO = userDAO.selectByPhone(phone);
        if(userPO != null){
            if(DesUtils.encrypt(password).equals(userPO.getPassword())){
                return Optional.of(System.currentTimeMillis() + "&" + userPO.getId() + "_" + DesUtils.encrypt(userPO.getId().toString()));
            }
        }
        return Optional.absent();
    }

    @Transactional
    @Override
    public void regist(String phone, String password, String name, String headPic) throws GisRuntimeException {
        UserPO userPO = new UserPO();
        userPO.setRoleId(-1);
        userPO.setPhone(phone);
        userPO.setPassword(DesUtils.encrypt(password));
        userPO.setName(name);
        userPO.setHeadPic(headPic);
        int a = userDAO.insert(userPO);
        if(a == 0){
            throw new GisRuntimeException("该手机号已存在,不能重复注册");
        }
    }

    @Transactional
    @Override
    public void updateRole(Integer userId, Integer roleId) {
        userDAO.updateRole(userId, roleId);
        userCache.invalidate(userId);
    }
}
