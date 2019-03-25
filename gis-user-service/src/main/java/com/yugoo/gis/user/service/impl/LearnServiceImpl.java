package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.LearnDAO;
import com.yugoo.gis.pojo.po.LearnPO;
import com.yugoo.gis.pojo.vo.LearnVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.ILearnService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author nihao 2019/3/22
 */
@Service
public class LearnServiceImpl implements ILearnService {

    @Autowired
    private LearnDAO learnDAO;

    @Override
    public void create(String title, String content, Integer sorting) {
        LearnPO learnPO = new LearnPO();
        learnPO.setTitle(title);
        learnPO.setContent(content);
        learnPO.setSorting(sorting);
        learnDAO.insert(learnPO);
    }

    @Override
    public void edit(Integer id, String title, String content, Integer sorting) {
        LearnPO learnPO = new LearnPO();
        learnPO.setId(id);
        learnPO.setTitle(title);
        learnPO.setContent(content);
        learnPO.setSorting(sorting);
        learnDAO.update(learnPO);
    }

    @Override
    public void delete(Integer id) {
        learnDAO.deleteById(id);
    }

    @Override
    public LearnVO getById(Integer id) {
        LearnPO learnPO = learnDAO.selectById(id);
        LearnVO learnVO = new LearnVO();
        BeanUtils.copyProperties(learnPO, learnVO);
        return learnVO;
    }

    @Override
    public ListVO<LearnVO> list(String title, Integer curPage, Integer pageSize) {
        long count = learnDAO.selectCount(title);
        ListVO<LearnVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<LearnPO> poList = learnDAO.select(title, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<LearnVO> voList = poList.stream().map(po -> {
                LearnVO vo = new LearnVO();
                BeanUtils.copyProperties(po, vo);
                return vo;
            }).collect(Collectors.toList());
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }
}
