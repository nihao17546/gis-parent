package com.yugoo.gis.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.common.constant.StreetType;
import com.yugoo.gis.common.exception.GisRuntimeException;
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

    @Override
    public void create(String name, String position, Integer type, String manager, String phone, byte[] pic, String remark, String competitor) {
        StreetPO check = streetDAO.selectByName(name);
        if (check != null) {
            throw new GisRuntimeException("该名称已经存在");
        }
        StreetPO streetPO = new StreetPO();
        streetPO.setName(name);
        streetPO.setPosition(position);
        streetPO.setType(type);
        streetPO.setManager(manager);
        streetPO.setPhone(phone);
        streetPO.setPic(pic);
        streetPO.setRemark(remark);
        streetPO.setCompetitor(competitor);
        streetDAO.insert(streetPO);
    }

    @Override
    public void update(Integer id, String name, String position, Integer type, String manager, String phone, byte[] pic, String remark, String competitor) {
        StreetPO check = streetDAO.selectByName(name);
        if (check != null && !check.getId().equals(id)) {
            throw new GisRuntimeException("该名称已经存在");
        }
        StreetPO streetPO = new StreetPO();
        streetPO.setId(id);
        streetPO.setName(name);
        streetPO.setPosition(position);
        streetPO.setType(type);
        streetPO.setManager(manager);
        streetPO.setPhone(phone);
        streetPO.setPic(pic);
        streetPO.setRemark(remark);
        streetPO.setCompetitor(competitor);
        streetDAO.update(streetPO);
    }

    @Override
    public StreetVO getById(Integer id) {
        StreetPO po = streetDAO.selectById(id);
        StreetVO vo = new StreetVO();
        BeanUtils.copyProperties(po, vo);
        vo.setTypeName(StreetType.getByValue(vo.getType()).getName());
        if (po.getCompetitor() != null && !po.getCompetitor().equals("")) {
            List<CompetitorVO> competitors = JSON.parseObject(po.getCompetitor(),
                    new TypeReference<List<CompetitorVO>>(){});
            vo.setCompetitors(competitors);
        }
        return vo;
    }
}
