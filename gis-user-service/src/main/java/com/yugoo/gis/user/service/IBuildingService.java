package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;

/**
 * @author nihao
 * @create 2018/10/5
 **/
public interface IBuildingService {
    ListVO<BuildingVO> list(Integer curPage, Integer pageSize, String name);
    void create(String name, Integer streetId, Double longitude, Double latitude);
    void update(Integer id, String name, Integer streetId, Double longitude, Double latitude);
    ListVO<BuildingVO> listByCenterId(Integer centerId);
}
