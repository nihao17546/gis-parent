package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.user.service.ITestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by nihao on 18/2/27.
 */
@Controller
public class TestController extends BaseController{
    @Resource
    private ITestService testService;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return ok().pull("data", testService.get()).json();
    }
}
