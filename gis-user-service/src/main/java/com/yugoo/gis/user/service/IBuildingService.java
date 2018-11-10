package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;

import java.util.List;

/**
 * @author nihao
 * @create 2018/10/5
 **/
public interface IBuildingService {
    ListVO<BuildingVO> list(Integer curPage, Integer pageSize, String name, Integer streetId);
    void create(String name, Integer streetId, Double longitude, Double latitude);
    void update(Integer id, String name, Integer streetId, Double longitude, Double latitude);
    ListVO<BuildingVO> listByCenterId(Integer centerId);
    List<BuildingVO> listOwn(Integer userId, String name);
    void delete(Integer id);
    List<BuildingVO> searchByName(String name);
    List<BuildingVO> searchFromMap(String name, Double loMin, Double loMax, Double laMin, Double laMax);
    BuildingVO getById(Integer id);
}
