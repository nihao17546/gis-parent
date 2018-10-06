package com.yugoo.gis.user.service.util;

import java.util.List;

/**
 * @author nihao
 * @create 2018/10/5
 **/
public class MapUtil {
    public static boolean isPtInPoly (double ALon , double ALat , List<List<Double>> ps) {
        int iSum, iCount, iIndex;
        double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon;
        if (ps.size() < 3) {
            return false;
        }
        iSum = 0;
        iCount = ps.size();
        for (iIndex = 0; iIndex < iCount; iIndex ++) {
            if (iIndex == iCount - 1) {
                dLon1 = ps.get(iIndex).get(0);
                dLat1 = ps.get(iIndex).get(1);
                dLon2 = ps.get(0).get(0);
                dLat2 = ps.get(0).get(1);
            } else {
                dLon1 = ps.get(iIndex).get(0);
                dLat1 = ps.get(iIndex).get(1);
                dLon2 = ps.get(iIndex + 1).get(0);
                dLat2 = ps.get(iIndex + 1).get(1);
            }
            // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
            if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1))) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    //得到 A点向左射线与边的交点的x坐标：
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat) ) / (dLat1 - dLat2);
                    // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：
                    if (dLon < ALon) {
                        iSum++;
                    }
                }
            }
        }
        if ((iSum % 2) != 0) {
            return true;
        }
        return false;
    }
}
