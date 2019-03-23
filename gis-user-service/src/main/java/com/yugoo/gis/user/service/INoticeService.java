package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.NoticeVO;

/**
 * @author nihao 2019/3/22
 */
public interface INoticeService {
    void create(String title, String content, Integer sorting);
    void edit(Integer id, String title, String content, Integer sorting);
    void delete(Integer id);
    NoticeVO getById(Integer id);
    ListVO<NoticeVO> list(String title, Integer curPage, Integer pageSize);
}
