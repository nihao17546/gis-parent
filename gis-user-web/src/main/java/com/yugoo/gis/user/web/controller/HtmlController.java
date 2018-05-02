package com.yugoo.gis.user.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by nihao on 18/2/27.
 */
@Controller
@RequestMapping("/html")
public class HtmlController extends BaseController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

}
