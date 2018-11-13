package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;

/**
 * @author nihao 2018/10/23
 */
public interface IResourceService {
    void create(Integer buildingId, String district, String floor, String number,
                Integer allPortCount, Integer idelPortCount, String sceneA,
                String sceneB, String overlayScene, Double longitude, Double latitude,
                String cityName, String streetName, String villageName, String admStreetName,
                String zoneName);
    void update(Integer id, Integer buildingId, String district, String floor, String number,
                Integer allPortCount, Integer idelPortCount, String sceneA,
                String sceneB, String overlayScene,
                String cityName, String streetName, String villageName, String admStreetName,
                String zoneName);
    ListVO<ResourceVO> list(Integer curPage, Integer pageSize, Integer buildingId);
    void delete(Integer id);
}
