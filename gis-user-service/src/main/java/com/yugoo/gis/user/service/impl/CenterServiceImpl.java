package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.dao.CenterDAO;
import com.yugoo.gis.dao.GroupDAO;
import com.yugoo.gis.dao.UserDAO;
import com.yugoo.gis.pojo.po.CenterPO;
import com.yugoo.gis.pojo.po.GroupPO;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.util.MapUtil;
import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.PointVO;
import com.yugoo.gis.user.service.ICenterService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/9/28
 */
@Service
public class CenterServiceImpl implements ICenterService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public ListVO<CenterVO> list(Integer curPage, Integer pageSize, String name) {
        long count = centerDAO.selectCount(name);
        ListVO<CenterVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<CenterPO> poList = centerDAO.select(name, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<Integer> groupIds = new ArrayList<>();
            List<CenterVO> voList = poList.stream().map(po -> {
                CenterVO vo = new CenterVO();
                BeanUtils.copyProperties(po, vo);
                if (vo.getRegion() != null && !vo.getRegion().equals("")) {
                    List<List<Double>> lists = JSON.parseObject(vo.getRegion(), new TypeReference<List<List<Double>>>(){});
                    List<PointVO> pointVOList = new ArrayList<>(lists.size());
                    for (List<Double> doubles : lists) {
                        PointVO pointVO = new PointVO();
                        pointVO.setLongitude(doubles.get(0));
                        pointVO.setLatitude(doubles.get(1));
                        pointVOList.add(pointVO);
                    }
                    vo.setPoints(pointVOList);
                }
                vo.setCenter(MapUtil.getCenterA(vo.getPoints()));
                if (!groupIds.contains(vo.getGroupId())) {
                    groupIds.add(vo.getGroupId());
                }
                return vo;
            }).collect(Collectors.toList());
            Map<Integer,GroupPO> groupPOMap = new HashMap<>();
            if (!groupIds.isEmpty()) {
                groupPOMap = groupDAO.selectByIds(groupIds);
            }
            for (CenterVO centerVO : voList) {
                if (groupPOMap.containsKey(centerVO.getGroupId())) {
                    centerVO.setGroupName(groupPOMap.get(centerVO.getGroupId()).getName());
                }
            }
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void delete(Integer id) {
        List<UserPO> userPOList = userDAO.selectByCenterId(id);
        if (!userPOList.isEmpty()) {
            throw new GisRuntimeException("该营销中心关联有用户，不能删除");
        }
        centerDAO.deleteById(id);
    }
}
