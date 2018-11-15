package com.yugoo.gis.user.service;

import com.google.common.base.Optional;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.UserInfoVO;
import com.yugoo.gis.pojo.vo.UserListVO;

import java.util.List;


/**
 * Created by nihao on 18/5/8.
 */
public interface IUserService {
    String create(String name, String phone, String password, Integer role, String department,
                Integer groupId, Integer centerId, String key, String number, String post);

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
     * @param number
     * @param post
     */
    void edit(Integer id, String name, String phone, String password, Integer role, String department,
              Integer groupId, Integer centerId, String key, String number, String post);

    void edit(Integer id, String name, String phone, String department);

    void edit(Integer id, String password);

    UserInfoVO getById(Integer id);
    Optional<String> login(String phone, String password, String key);

    ListVO<UserListVO> list(Integer curPage, Integer pageSize, String phone, String name);
    List<UserListVO> getSubordinates(Integer id, String searchParam);
}
