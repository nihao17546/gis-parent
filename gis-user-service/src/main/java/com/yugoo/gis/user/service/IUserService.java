package com.yugoo.gis.user.service;

import com.google.common.base.Optional;
import com.yugoo.gis.pojo.vo.UserInfoVO;


/**
 * Created by nihao on 18/5/8.
 */
public interface IUserService {
    void create(String name, String phone, String password, Integer role, String department,
                Integer groupId, Integer centerId, String key);

    /**
     * 后台编辑
     * @param id
     * @param name
     * @param phone
     * @param password
     * @param role
     * @param department
     * @param groupId
     * @param centerId
     * @param key
     */
    void edit(Integer id, String name, String phone, String password, Integer role, String department,
              Integer groupId, Integer centerId, String key);

    void edit(Integer id, String name, String phone, String department);

    void edit(Integer id, String password);

    UserInfoVO getById(Integer id);
    Optional<String> login(String phone, String password);
}
