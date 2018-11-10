package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.ResourceVO;

/**
 * @author nihao 2018/10/23
 */
public interface IResourceService {
    void create(Integer buildingId, String district, String floor, String number,
                Integer allPortCount, Integer idelPortCount, String sceneA,
                String sceneB, String overlayScene, Double longitude, Double latitude);
    void update(Integer id, Integer buildingId, String district, String floor, String number,
                Integer allPortCount, Integer idelPortCount, String sceneA,
                String sceneB, String overlayScene);
    ListVO<ResourceVO> list(Integer curPage, Integer pageSize, Integer buildingId);
    void delete(Integer id);
}
