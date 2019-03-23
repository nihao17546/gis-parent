package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.NoticeDAO;
import com.yugoo.gis.pojo.po.NoticePO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.NoticeVO;
import com.yugoo.gis.user.service.INoticeService;
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
public class NoticeServiceImpl implements INoticeService {

    @Autowired
    private NoticeDAO noticeDAO;

    @Override
    public void create(String title, String content, Integer sorting) {
        NoticePO noticePO = new NoticePO();
        noticePO.setTitle(title);
        noticePO.setContent(content);
        noticePO.setSorting(sorting);
        noticeDAO.insert(noticePO);
    }

    @Override
    public void edit(Integer id, String title, String content, Integer sorting) {
        NoticePO noticePO = new NoticePO();
        noticePO.setId(id);
        noticePO.setTitle(title);
        noticePO.setContent(content);
        noticePO.setSorting(sorting);
        noticeDAO.update(noticePO);
    }

    @Override
    public void delete(Integer id) {
        noticeDAO.deleteById(id);
    }

    @Override
    public NoticeVO getById(Integer id) {
        NoticePO noticePO = noticeDAO.selectById(id);
        NoticeVO noticeVO = new NoticeVO();
        BeanUtils.copyProperties(noticePO, noticeVO);
        return noticeVO;
    }

    @Override
    public ListVO<NoticeVO> list(String title, Integer curPage, Integer pageSize) {
        long count = noticeDAO.selectCount(title);
        ListVO<NoticeVO> listVO = new ListVO<>(curPage, pageSize);
        if (count > 0) {
            List<NoticePO> poList = noticeDAO.select(title, new RowBounds((curPage - 1) * pageSize, pageSize));
            List<NoticeVO> voList = poList.stream().map(po -> {
                NoticeVO vo = new NoticeVO();
                BeanUtils.copyProperties(po, vo);
                return vo;
            }).collect(Collectors.toList());
            listVO.setList(voList);
            listVO.setTotalCount(count);
        }
        return listVO;
    }
}
