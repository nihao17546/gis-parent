package com.yugoo.gis.user.web.controller;

import com.google.common.base.Optional;
import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.common.exception.GisRuntimeException;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.pojo.vo.ListVO;
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
     * @param name
     * @param department
     * @param password
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/regist")
    public String regist(@RequestParam(value = "phone") String phone,
                         @RequestParam(value = "name") String name,
                         @RequestParam(value = "department") String department,
                         @RequestParam(value = "password") String password,
                         HttpServletResponse response,
                         HttpServletRequest request){
        try{
            userService.regist(phone, password, name, department);
        }catch (GisRuntimeException e){
            return fail(e.getMessage()).json();
        }
        return ok("注册成功").json();
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

    @RequestMapping("/list")
    public String list(@Value("#{request.getAttribute('uid')}") Integer uid,
                       @RequestParam(value = "curPage", required = false, defaultValue = "1") Integer curPage,
                       @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "department", required = false) String department,
                       @RequestParam(value = "phone", required = false) String phone){
        ListVO<UserPO> listVO = userService.getPagination(curPage, pageSize, name, department, phone);
        return ok().pull("result", listVO).json();
    }

    @RequestMapping("/updateInfo")
    public String updateInfo(@Value("#{request.getAttribute('uid')}") Integer uid,
                             @RequestParam(value = "phone", required = false) String phone,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "department", required = false) String department){
        if(phone != null && phone.trim().equals(""))
            phone = null;
        if(name != null && name.trim().equals(""))
            name = null;
        if(department != null && department.trim().equals(""))
            department = null;
        try{
            userService.updateInfo(uid, phone, name, department);
        }catch (GisRuntimeException e){
            return fail(e.getMessage()).json();
        }
        return ok("更新成功").json();
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(@Value("#{request.getAttribute('uid')}") Integer uid,
                                 @RequestParam(value = "newPassword", required = true) String newPassword,
                                 @RequestParam(value = "oldPassword", required = true) String oldPassword){
        try{
            userService.updatePassword(uid, newPassword, oldPassword);
        }catch (GisRuntimeException e){
            return fail(e.getMessage()).json();
        }
        return ok("更新成功").json();
    }
}
