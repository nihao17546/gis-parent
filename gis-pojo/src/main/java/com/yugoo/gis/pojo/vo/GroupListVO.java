package com.yugoo.gis.pojo.vo;

import lombok.Data;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @author nihao 2018/9/27
 */
@Data
public class GroupListVO {
    private Integer id;
    private String name;
    private String position;
    private Date ctime;

    private String managerName;
    private String managerPhone;
    private List<PointVO> centerPoints;
    private PointVO center;

    private DecimalFormat df = new DecimalFormat("0.000000");
    public void applyCenter() {
        if (centerPoints != null && !centerPoints.isEmpty()) {
            int size = centerPoints.size();
            double allLongitude = 0.0;
            double allLatitude = 0.0;
            for (PointVO pointVO : centerPoints) {
                allLongitude += pointVO.getLongitude();
                allLatitude += pointVO.getLatitude();
            }
            PointVO cen = new PointVO();
            cen.setLongitude(Double.parseDouble(df.format(allLongitude / size)));
            cen.setLatitude(Double.parseDouble(df.format(allLatitude / size)));
            this.center = cen;
        }
    }
}
