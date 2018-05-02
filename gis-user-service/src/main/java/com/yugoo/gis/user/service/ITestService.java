package com.yugoo.gis.user.service;

import com.yugoo.gis.pojo.po.TestPO;

/**
 * Created by nihao on 18/2/27.
 */
public interface ITestService {
    TestPO get();
    void testTx(String name);
}
