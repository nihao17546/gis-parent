package com.yugoo.gis.user.service.impl;

import com.yugoo.gis.dao.ITestDAO;
import com.yugoo.gis.user.service.ITestService2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by nihao on 18/3/6.
 */
@Service
public class TestService2Impl implements ITestService2 {
    @Resource
    private ITestDAO testDAO;

    @Transactional
    @Override
    public void test(String name) {
        testDAO.update(name);
        throw new RuntimeException("dasdas");
    }
}
