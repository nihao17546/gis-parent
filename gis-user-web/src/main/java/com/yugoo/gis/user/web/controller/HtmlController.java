package com.yugoo.gis.user.web.controller;

import com.yugoo.gis.pojo.vo.UserInfoVO;
import com.yugoo.gis.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IUserService userService;

    @RequestMapping("/{page}.html")
    public String index(@PathVariable String page, Model model,
                        HttpServletRequest request){
        model.addAttribute("contextPath", request.getContextPath());
        if (page.equals("index")) {
            Integer uid = (Integer) request.getAttribute("uid");
            UserInfoVO infoVO = userService.getById(uid);
            model.addAttribute("own", infoVO);
        }
        return page;
    }

}
