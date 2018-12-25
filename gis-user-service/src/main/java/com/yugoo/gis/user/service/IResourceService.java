package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.excel.ResourceImport;
import com.yugoo.gis.pojo.po.ResourcePO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;

import java.util.List;

/**
 * @author nihao 2018/10/23
 */
public interface IResourceService {
    void create(Integer buildingId, String district, Integer floor, String number,
                Integer allPortCount, Integer idelPortCount, String sceneA,
                String sceneB, String overlayScene, Double longitude, Double latitude,
                String cityName, String streetName, String villageName, String admStreetName,
                String zoneName, String primaryId);
    void update(Integer id, Integer buildingId, String district, Integer floor, String number,
                Integer allPortCount, Integer idelPortCount, String sceneA,
                String sceneB, String overlayScene,
                String cityName, String streetName, String villageName, String admStreetName,
                String zoneName, String primaryId);
    ListVO<ResourceVO> list(Integer curPage, Integer pageSize, Integer buildingId);
    void delete(Integer id);
    String importData(List<ResourceImport> list);
    List<ResourceVO> searchFromMap(String streetName, Double loMin, Double loMax, Double laMin, Double laMax);
    ResourceVO getById(Integer id);
}
