import com.yugoo.gis.pojo.po1.UserPO;
import com.yugoo.gis.user.web.result.JsonResult;
import org.junit.Test;

import java.io.*;

/**
 * Created by nihao on 18/3/17.
 */
public class SimpleTest {
    @Test
    public void dasdas(){
        UserPO userPO = new UserPO();
        userPO.setName("dasda");
        userPO.setPassword("123");
        String s = JsonResult.success().pull("a",userPO).json();
        System.out.println("---");
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
}
