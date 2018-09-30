package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.pojo.vo.StreetVO;

/**
 * @author nihao 2018/9/30
 */
public interface IStreetService {
    ListVO<StreetVO> list(Integer curPage, Integer pageSize, String name);
}
