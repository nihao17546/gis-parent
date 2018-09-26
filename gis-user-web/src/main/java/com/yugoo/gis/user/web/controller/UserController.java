package com.yugoo.gis.user.web.controller;

import com.google.common.base.Optional;
import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nihao on 18/5/8.
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * 管理员在后台添加新用户
     * @param name
     * @param phone
     * @param department
     * @param key
     * @param role
     * @param groupId
     * @param centerId
     * @param password
     * @return
     */
    @RequestMapping("/create")
    public String create(@RequestParam String name, @RequestParam String phone,
                           @RequestParam(required = false) String department,
                           @RequestParam(required = false) String key,
                           @RequestParam Integer role, @RequestParam(required = false) Integer groupId,
                           @RequestParam(required = false) Integer centerId,
                           @RequestParam(required = false) String password) {
        try {
            userService.create(name, phone, password, role, department, groupId, centerId, key);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    /**
     * 管理员在后台编辑用户
     * @param name
     * @param phone
     * @param department
     * @param key
     * @param role
     * @param groupId
     * @param centerId
     * @param password
     * @param id
     * @return
     */
    @RequestMapping("/edit")
    public String edit(@RequestParam String name, @RequestParam String phone,
                       @RequestParam(required = false) String department,
                       @RequestParam(required = false) String key,
                       @RequestParam Integer role, @RequestParam(required = false) Integer groupId,
                       @RequestParam(required = false) Integer centerId,
                       @RequestParam(required = false) String password,
                       @RequestParam Integer id) {
        try {
            userService.edit(id, name, phone, password, role, department, groupId, centerId, key);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    /**
     * 管理员在后台查看用户个人信息
     * @param id
     * @return
     */
    @RequestMapping("/info")
    public String info(@RequestParam Integer id) {
        return ok().pull("info", userService.getById(id)).json();
    }

    /**
     * 用户查看自己的信息
     * @param uid
     * @return
     */
    @RequestMapping("/ownInfo")
    public String ownInfo(@Value("#{request.getAttribute('uid')}") Integer uid) {
        return ok().pull("info", userService.getById(uid)).json();
    }

    /**
     * 用户更新个人资料
     * @param id
     * @param name
     * @param phone
     * @param department
     * @return
     */
    @RequestMapping("/update")
    public String update(@RequestParam Integer id, @RequestParam String name,
                         @RequestParam String phone, @RequestParam(required = false) String department) {
        try {
            userService.edit(id, name, phone, department);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        return ok().json();
    }

    /**
     * 用户更新密码
     * @param id
     * @param password
     * @return
     */
    @RequestMapping("/updatePassword")
    public String update(@RequestParam Integer id, @RequestParam String password) {
        userService.edit(id, password);
        return ok().json();
    }

    /**
     * pc端登录
     * @param phone
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/login/pc")
    public String login(@RequestParam String phone, @RequestParam String password,
                        HttpServletRequest request, HttpServletResponse response) {
        Optional<String> optional = userService.login(phone, password, null);
        if(!optional.isPresent()){
            return fail("账号或密码错误").json();
        }
        // 写入cookie
        Cookie cookie = new Cookie(StaticConstant.cookieName, optional.get());
        cookie.setPath("/");
        response.addCookie(cookie);
        return ok("登录成功").pull("token", optional.get()).json();
    }

    /**
     * 移动端登录
     * @param phone
     * @param password
     * @param key
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/login/mobile")
    public String login(@RequestParam String phone, @RequestParam String password,
                        @RequestParam String key,
                        HttpServletRequest request, HttpServletResponse response) {
        Optional<String> optional = null;
        try {
            optional = userService.login(phone, password, key);
        } catch (GisRuntimeException e) {
            return fail(e.getMessage()).json();
        }
        if(!optional.isPresent()){
            return fail("账号或密码错误").json();
        }
        // 写入cookie
        Cookie cookie = new Cookie(StaticConstant.cookieName, optional.get());
        cookie.setPath("/");
        response.addCookie(cookie);
        return ok("登录成功").pull("token", optional.get()).json();
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(StaticConstant.cookieName.equals(cookie.getName())){
                    Cookie cookie1 = new Cookie(StaticConstant.cookieName,"");
                    cookie1.setPath("/");
                    cookie1.setMaxAge(0);
                    response.addCookie(cookie1);
                }
            }
        }
        return ok().json();
    }


}
