import com.yugoo.gis.pojo.vo.ConsumerListVO;
import com.yugoo.gis.pojo.vo.ListVO;
import com.yugoo.gis.user.service.IStatisticService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by nihao on 18/3/6.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class MainTest {
    @Autowired
    private IStatisticService statisticService;

    @Test
    public void ascasc() {
    }
}
