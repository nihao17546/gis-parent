package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.CenterVO;
import com.yugoo.gis.pojo.vo.ListVO;

/**
 * @author nihao 2018/9/26
 */
public interface ICenterService {
    ListVO<CenterVO> list(Integer curPage, Integer pageSize, String name);
    void delete(Integer id);
}
