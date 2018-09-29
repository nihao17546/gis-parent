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
import com.yugoo.gis.pojo.vo.GroupListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.PointVO;
import com.yugoo.gis.user.service.IGroupService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/9/27
 */
@Service
public class GroupServiceImpl implements IGroupService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GroupDAO groupDAO;
    @Autowired
    private CenterDAO centerDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public ListVO<GroupListVO> list(Integer curPage, Integer pageSize, String name) {
        long count = groupDAO.selectCount(name);
        ListVO<GroupListVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<GroupPO> poList = groupDAO.select(name, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<GroupListVO> groupListVOList = poList.stream().map(po -> {
                GroupListVO vo = new GroupListVO();
                BeanUtils.copyProperties(po, vo);
                List<CenterPO> centerPOList = centerDAO.selectByGroupId(vo.getId());
                if (centerPOList.isEmpty()) {
                    vo.setCenterPoints(new ArrayList<>());
                }
                else {
                    List<PointVO> pointVOList = new ArrayList<>();
                    for (CenterPO centerPO : centerPOList) {
                        if (centerPO.getRegion() != null && !centerPO.getRegion().equals("")) {
                            List<List<Double>> lists = JSON.parseObject(centerPO.getRegion(), new TypeReference<List<List<Double>>>(){});
                            PointVO pointVO = MapUtil.getCenterB(lists);
                            pointVO.setName(centerPO.getName());
                            pointVOList.add(pointVO);
                        }
                    }
                    vo.setCenterPoints(pointVOList);
                }
                vo.setCenter(MapUtil.getCenterA(vo.getCenterPoints()));
                UserPO userPO = userDAO.selectManager(vo.getId());
                if (userPO != null) {
                    vo.setManagerName(userPO.getName());
                    vo.setManagerPhone(userPO.getPhone());
                }
                return vo;
            }).collect(Collectors.toList());
            listVO.setList(groupListVOList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }

    @Override
    public void delete(Integer id) {
        List<CenterPO> centerPOList = centerDAO.selectByGroupId(id);
        if (!centerPOList.isEmpty()) {
            throw new GisRuntimeException("该要客组关联有营销中心，不能删除");
        }
        List<UserPO> userPOList = userDAO.selectByGroupId(id);
        if (!userPOList.isEmpty()) {
            throw new GisRuntimeException("该要客组关联有用户，不能删除");
        }
        groupDAO.deleteById(id);
    }
}
