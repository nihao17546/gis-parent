package com.yugoo.gis.user.web.controller;

import com.google.common.base.Optional;
import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.user.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 账号密码登录
     * @param phone
     * @param password
     * @param response
     * @param request
     * @return
     */
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

    /**
     * 注册
     * @param phone
     * @param password
     * @param name
     * @param headPic
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/regist")
    public String regist(@RequestParam(value = "phone") String phone,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "headPic", required = false) String headPic,
                         HttpServletResponse response,
                         HttpServletRequest request){
        try{
            userService.regist(phone, password, name, headPic);
        }catch (GisRuntimeException e){
            return fail(e.getMessage()).json();
        }
        return login(phone, password, response, request);
    }

    /**
     * 授权角色
     * @param uid
     * @param userId
     * @param roleId
     * @return
     */
    @RequestMapping("/authRole")
    public String authRole(@Value("#{request.getAttribute('uid')}") Integer uid,
                           @RequestParam Integer userId,
                           @RequestParam Integer roleId){
        if(uid.equals(userId))
            return fail("不能对自己操作").json();
        userService.updateRole(userId, roleId);
        return ok("操作成功").json();
    }
}
