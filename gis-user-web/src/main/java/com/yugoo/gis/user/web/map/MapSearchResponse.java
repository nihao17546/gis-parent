package com.yugoo.gis.user.web.map;

import lombok.Data;

import java.util.List;

/**
 * @author nihao 2018/10/26
 */
@Data
public class MapSearchResponse {
    private Integer status;
    private String message;
    private Integer total;
    private List<MapSearchResult> results;
}
