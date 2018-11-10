package com.yugoo.gis.user.web.map;

import lombok.Data;

/**
 * @author nihao 2018/10/26
 */
@Data
public class MapSearchResult {
    private String name;
    private String uid;
    private String address;
    private String province;
    private String city;
    private String area;
    private String telephone;
    private MapSearchLocation location;
}
