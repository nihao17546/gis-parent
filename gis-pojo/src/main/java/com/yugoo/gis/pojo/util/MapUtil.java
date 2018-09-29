package com.yugoo.gis.pojo.util;

import com.yugoo.gis.pojo.vo.PointVO;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author nihao 2018/9/28
 */
public class MapUtil {
    private static final DecimalFormat df = new DecimalFormat("0.000000");
    public static final PointVO getCenterA(List<PointVO> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        double allLongitude = 0.0;
        double allLatitude = 0.0;
        for (PointVO pointVO : list) {
            allLongitude += pointVO.getLongitude();
            allLatitude += pointVO.getLatitude();
        }
        PointVO center = new PointVO();
        center.setLongitude(Double.parseDouble(df.format(allLongitude / size)));
        center.setLatitude(Double.parseDouble(df.format(allLatitude / size)));
        return center;
    }
    public static final PointVO getCenterB(List<List<Double>> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int size = list.size();
        double allLongitude = 0.0;
        double allLatitude = 0.0;
        for (List<Double> doubles : list) {
            allLongitude += doubles.get(0);
            allLatitude += doubles.get(1);
        }
        PointVO center = new PointVO();
        center.setLongitude(Double.parseDouble(df.format(allLongitude / size)));
        center.setLatitude(Double.parseDouble(df.format(allLatitude / size)));
        return center;
    }
}
