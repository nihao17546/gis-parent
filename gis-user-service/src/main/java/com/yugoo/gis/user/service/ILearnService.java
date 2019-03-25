package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.LearnVO;

/**
 * @author nihao 2019/3/22
 */
public interface ILearnService {
    void create(String title, String content, Integer sorting);
    void edit(Integer id, String title, String content, Integer sorting);
    void delete(Integer id);
    LearnVO getById(Integer id);
    ListVO<LearnVO> list(String title, Integer curPage, Integer pageSize);
}
