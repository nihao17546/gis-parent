package com.yugoo.gis.user.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.common.utils.DesUtils;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IUserService;
import com.yugoo.gis.user.service.cache.CacheManager;
import com.yugoo.gis.user.service.cache.CacheManagerUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by nihao on 18/5/8.
 */
@Service
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserDAO userDAO;

    private Integer defaultRoleId = 1;

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
    public void regist(String phone, String password, String name, String department) throws GisRuntimeException {
        UserPO userPO = new UserPO();
        userPO.setRoleId(-1);
        userPO.setPhone(phone);
        userPO.setPassword(DesUtils.encrypt(password));
        userPO.setName(name);
        userPO.setDepartment(department);
        userPO.setRoleId(defaultRoleId);
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

    @Override
    public ListVO<UserPO> getPagination(Integer curPage, Integer pageSize, String name, String department, String phone) {
        long totalCount = userDAO.selectCount(name, department, phone);
        ListVO<UserPO> listVO = new ListVO<>(curPage, pageSize);
        listVO.setTotalCount(totalCount);
        if(totalCount > 0){
            listVO.setTotalPage((int)(totalCount / pageSize) + ((totalCount % pageSize) > 0 ? 1 : 0));
            RowBounds rowBounds = new RowBounds((curPage - 1) * pageSize, pageSize);
            List<UserPO> list = userDAO.selectList(name, department, phone, "date", "desc", rowBounds);
            listVO.setList(list);
        }
        return listVO;
    }

    @Transactional
    @Override
    public void updateInfo(Integer uid, String phone, String name, String department) throws GisRuntimeException {
        if(phone != null){
            UserPO checkPO = userDAO.selectByPhone(phone);
            if(checkPO != null && !checkPO.getId().equals(uid)){
                throw new GisRuntimeException("该手机号已存在");
            }
        }
        UserPO userPO = new UserPO();
        userPO.setId(uid);
        userPO.setPhone(phone);
        userPO.setName(name);
        userPO.setDepartment(department);
        userDAO.update(userPO);
        userCache.invalidate(uid);
    }

    @Transactional
    @Override
    public void updatePassword(Integer uid, String newPassword, String oldPassword) throws GisRuntimeException {
        int a = userDAO.updatePassword(uid, DesUtils.encrypt(newPassword), DesUtils.encrypt(oldPassword));
        if(a != 1){
            throw new GisRuntimeException("旧密码校验失败");
        }
    }
}
