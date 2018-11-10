package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ListVO;

import java.util.List;

/**
 * @author nihao 2018/9/26
 */
public interface ICenterService {
    ListVO<CenterVO> list(Integer curPage, Integer pageSize, String name, Integer groupId);
    void delete(Integer id);
    void create(String name, Integer groupId, String manager, String phone, String position,
                String district, String region,
                Double loMax, Double loMin, Double laMax, Double laMin);
    void update(Integer id, String name, Integer groupId, String manager, String phone, String position,
              String district, String region,
                Double loMax, Double loMin, Double laMax, Double laMin);
    List<CenterVO> searchByName(String name);
    List<CenterVO> searchFromMap(String name, Double loMin, Double loMax, Double laMin, Double laMax, Integer groupId);
    CenterVO getById(Integer id);
}
