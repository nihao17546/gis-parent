package com.yugoo.gis.user.service.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.dao.BuildingDAO;
import com.yugoo.gis.dao.StreetDAO;
import com.yugoo.gis.pojo.po.BuildingPO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.StreetPO;
import com.yugoo.gis.pojo.vo.BuildingVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.util.MapUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author nihao 2019/4/30
 */
@Component
public class ReferenceCache {
    private volatile CacheManager<Integer,SoftReference<List<BuildingVO>>> cacheManager = CacheManager.build(30L, TimeUnit.MINUTES);

    @Autowired
    private BuildingDAO buildingDAO;

    @Autowired
    private StreetDAO streetDAO;

    public ListVO<BuildingVO> getBuildingByCenter(CenterPO centerPO, Integer curPage, Integer pageSize) {
        List<BuildingVO> list = init(centerPO);
        List<BuildingVO> voList = pagination(curPage, pageSize, list);
        ListVO<BuildingVO> listVO = new ListVO<>(curPage, pageSize);
        listVO.setList(voList);
        listVO.setTotalCount(list.size());
        return listVO;
    }

    private List pagination(Integer curPage, Integer pageSize, List list) {
        Integer from = (curPage - 1) * pageSize;
        Integer to = from + pageSize;
        if (list.size() >= to) {
            return list.subList(from, to);
        } else if (list.size() >= from) {
            return list.subList(from, list.size());
        } else {
            return new ArrayList<>();
        }
    }

    private List<BuildingVO> init(CenterPO centerPO) {
        SoftReference<List<BuildingVO>> reference = cacheManager.getIfPresent(centerPO.getId());
        if (reference != null) {
            List<BuildingVO> list = reference.get();
            if (list != null) {
                return list;
            }
        }
        synchronized (ReferenceCache.class) {
            reference = cacheManager.getIfPresent(centerPO.getId());
            if (reference != null) {
                List<BuildingVO> list = reference.get();
                if (list != null) {
                    return list;
                }
            }
            List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
            List<BuildingPO> buildingPOList = buildingDAO.selectAllByLoAndLa(centerPO.getLoMin(), centerPO.getLoMax(),
                    centerPO.getLaMin(), centerPO.getLaMax());
            List<BuildingVO> voList = new ArrayList<>();
            List<Integer> streetIds = new ArrayList<>();
            for (BuildingPO buildingPO : buildingPOList) {
                if (MapUtil.isPtInPoly(buildingPO.getLongitude(), buildingPO.getLatitude(), lists)) {
                    BuildingVO vo = new BuildingVO();
                    BeanUtils.copyProperties(buildingPO, vo);
                    if (buildingPO.getStreetId() != null && !streetIds.contains(buildingPO.getStreetId())) {
                        streetIds.add(buildingPO.getStreetId());
                    }
                    voList.add(vo);
                }
            }
            if (!streetIds.isEmpty()) {
                Map<Integer,StreetPO> streetPOMap = streetDAO.selectByIds(streetIds);
                for (BuildingVO vo : voList) {
                    if (streetPOMap.containsKey(vo.getStreetId())) {
                        vo.setStreetName(streetPOMap.get(vo.getStreetId()).getName());
                    }
                }
            }
            cacheManager.set(centerPO.getId(), new SoftReference<>(voList));
            return voList;
        }
    }
}
