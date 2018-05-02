package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.ITestDAO;
import com.yugoo.gis.pojo.po.TestPO;
import com.yugoo.gis.user.service.ITestService;
import com.yugoo.gis.user.service.ITestService2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by nihao on 18/2/27.
 */
@Service
public class TestServiceImpl implements ITestService {

    @Resource
    private ITestDAO testDAO;
    @Resource
    private ITestService2 testService2;

    @Override
    public TestPO get() {
        return testDAO.select();
    }


    @Override
    public void testTx(String name) {
        testService2.test(name);
    }

    @Transactional
    private void qwe(String name){
        testDAO.update(name);
        throw new RuntimeException("123456");
    }
}
