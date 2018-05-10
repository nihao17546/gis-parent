package com.yugoo.gis.user.service;

import com.google.common.base.Optional;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ListVO;

/**
 * Created by nihao on 18/5/8.
 */
public interface IUserService {
    UserPO getUserById(Integer userId);
    Optional<String> login(String phone, String password);
    void regist(String phone, String password, String name, String department) throws GisRuntimeException;
    void updateRole(Integer userId, Integer roleId);
    ListVO<UserPO> getPagination(Integer curPage, Integer pageSize, String name, String department, String phone);
    void updateInfo(Integer uid, String phone, String name, String department) throws GisRuntimeException;
    void updatePassword(Integer uid, String newPassword, String oldPassword) throws GisRuntimeException;
}
