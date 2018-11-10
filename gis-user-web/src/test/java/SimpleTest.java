import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.PointVO;
import com.yugoo.gis.pojo.vo.ResourceVO;
import com.yugoo.gis.user.service.util.MapUtil;
import com.yugoo.gis.user.web.result.JsonResult;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nihao on 18/3/17.
 */
public class SimpleTest {
    @Test
    public void dasdas(){
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setNumber("123");
        System.out.println("--");
    }

    @Test
    public void sdas() throws Exception {
        File file = new File("/Users/nihao/das");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            String ss = tempString.substring(0,5);
            System.out.println(ss);
            if(line % 30 == 0){
                System.out.println("--------------------------");
            }
//            System.out.println(ss+".cn");
//            System.out.println(ss+".net");
//            System.out.println(ss+".com");
//            System.out.println(ss+".com");
//            System.out.println(ss+".com");
//            System.out.println(ss+".com");
//            System.out.println(ss+".com");
//            System.out.println(ss+".com");
//            ximalaya.net
//            ximalaya.org
//            ximalaya.info
//            ximalaya.net.cn
//            ximalaya.name
//            ximalaya.mobi
//            ximalaya.wang
//            ximalaya.club
//            ximalaya.ac.cn
//            ximalaya.xyz
//            ximalaya.com
//            ximalaya.cn
            line++;
        }
        reader.close();
    }

    @Test
    public void dasadsddas() {
        String aa = "[[104.054543,30.593316],[104.068413,30.596176],[104.070713,30.588715],[104.063455,30.586166]]";
        List<List<Double>> lists = JSON.parseObject(aa, new TypeReference<List<List<Double>>>(){});
        System.out.println("---");
    }

    @Test
    public void sacascascasca() {
        String ss = "[[103.805302,30.682201],[103.810189,30.674499],[103.814788,30.66605],[103.818525,30.6576],[103.834623,30.665056],[103.85072,30.671517],[103.844971,30.681953],[103.840516,30.688102],[103.838935,30.690338],[103.835198,30.696425],[103.82025,30.690338],[103.82025,30.690338]]";
        List<List<Double>> lists = JSON.parseObject(ss, new TypeReference<List<List<Double>>>(){});
        boolean b = MapUtil.isPtInPoly(103.808659,30.682742, lists);
        System.out.println(b);
    }
}
