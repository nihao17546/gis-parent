package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StreetVO;

/**
 * @author nihao 2018/9/30
 */
public interface IStreetService {
    ListVO<StreetVO> list(Integer curPage, Integer pageSize, String name);
    void create(String name, String position, Integer type, String manager,
                String phone, byte[] pic, String remark, String competitor);
    void update(Integer id, String name, String position, Integer type, String manager,
                String phone, byte[] pic, String remark, String competitor);
    StreetVO getById(Integer id);
}
