package com.yugoo.gis.user.web.controller;

import com.google.common.base.Optional;
import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.user.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nihao on 18/5/8.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private IUserService userService;

    @RequestMapping("/login")
    public String login(@RequestParam("phone") String phone,
                        @RequestParam("password") String password,
                        HttpServletResponse response,
                        HttpServletRequest request){
        Optional<String> result = userService.login(phone, password);
        if(!result.isPresent()){
            return fail("账号或密码错误").json();
        }
        Cookie cookie = new Cookie(StaticConstant.cookieName, result.get());
        cookie.setPath("/");
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
        return ok("登录成功").json();
    }
}
