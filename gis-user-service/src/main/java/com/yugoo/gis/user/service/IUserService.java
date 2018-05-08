package com.yugoo.gis.user.service;

import com.google.common.base.Optional;
import com.yugoo.gis.pojo.po.UserPO;

/**
 * Created by nihao on 18/5/8.
 */
public interface IUserService {
    UserPO getUserById(Integer userId);
    Optional<String> login(String phone, String password);
}
