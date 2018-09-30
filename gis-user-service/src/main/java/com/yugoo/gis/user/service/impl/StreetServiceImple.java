package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.constant.StreetType;
import com.yugoo.gis.dao.StreetDAO;
import com.yugoo.gis.pojo.po.StreetPO;
import com.yugoo.gis.pojo.vo.CompetitorVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StreetVO;
import com.yugoo.gis.user.service.IStreetService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao 2018/9/30
 */
@Service
public class StreetServiceImple implements IStreetService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StreetDAO streetDAO;

    @Override
    public ListVO<StreetVO> list(Integer curPage, Integer pageSize, String name) {
        long count = streetDAO.selectCount(name);
        ListVO<StreetVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<StreetPO> streetPOList = streetDAO.select(name, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<StreetVO> streetVOList = streetPOList.stream().map(po -> {
                StreetVO vo = new StreetVO();
                BeanUtils.copyProperties(po, vo);
                vo.setTypeName(StreetType.getByValue(vo.getType()).getName());
                if (po.getCompetitor() != null && !po.getCompetitor().equals("")) {
                    List<CompetitorVO> competitors = JSON.parseObject(po.getCompetitor(),
                            new TypeReference<List<CompetitorVO>>(){});
                    vo.setCompetitors(competitors);
                }
                return vo;
            }).collect(Collectors.toList());
            listVO.setList(streetVOList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }
}
