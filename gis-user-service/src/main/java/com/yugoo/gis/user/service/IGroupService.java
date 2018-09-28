package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.GroupListVO;
import com.yugoo.gis.pojo.vo.ListVO;

/**
 * @author nihao 2018/9/20
 */
public interface IGroupService {
    ListVO<GroupListVO> list(Integer curPage, Integer pageSize, String name);
    void delete(Integer id);
}
