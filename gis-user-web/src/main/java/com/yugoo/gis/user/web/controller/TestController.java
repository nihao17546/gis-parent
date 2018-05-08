package com.yugoo.gis.user.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by nihao on 18/5/8.
 */
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {
    @RequestMapping("/")
    public String in(){
        return "dsada";
    }
    @RequestMapping("/qwe")
    public String qwe(){
        return "dsada";
    }
}
