import com.yugoo.gis.dao.ITestDAO;
import com.yugoo.gis.pojo.po.TestPO;
import com.yugoo.gis.user.service.ITestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * Created by nihao on 18/3/6.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class MainTest {
    @Resource
    private ITestService testService;
    @Resource
    private ITestDAO testDAO;

    @Test
    public void dasdasd(){
        int a = testDAO.uu(new BigDecimal("0.999998"));
        TestPO s = testDAO.select();
        testService.testTx("888");
    }
}
