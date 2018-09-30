package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ListVO;

/**
 * @author nihao 2018/9/26
 */
public interface ICenterService {
    ListVO<CenterVO> list(Integer curPage, Integer pageSize, String name);
    void delete(Integer id);
    void create(String name, Integer groupId, String manager, String phone, String position,
                String district, String region);
    void update(Integer id, String name, Integer groupId, String manager, String phone, String position,
              String district, String region);
}
