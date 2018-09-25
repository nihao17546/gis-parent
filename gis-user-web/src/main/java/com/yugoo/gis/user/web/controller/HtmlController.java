package com.yugoo.gis.user.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by nihao on 18/2/27.
 */
@Controller
public class HtmlController extends BaseController {

    @RequestMapping("/{page}.html")
    public String index(@PathVariable String page, Model model,
                        HttpServletRequest request){
        model.addAttribute("contextPath", request.getContextPath());
        return page;
    }

}
