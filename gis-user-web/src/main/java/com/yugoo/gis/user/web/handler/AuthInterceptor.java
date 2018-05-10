package com.yugoo.gis.user.web.handler;

import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.pojo.po.UserPO;
import com.yugoo.gis.user.service.IResourceService;
import com.yugoo.gis.user.service.IUserService;
import com.yugoo.gis.user.web.result.JsonResult;
import com.yugoo.gis.user.web.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by nihao on 18/5/8.
 */
public class AuthInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private IResourceService resourceService;
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        logger.debug("request[{}]", servletPath);
        Integer userId = CookieUtils.getUserId(request);
        if(userId == null){
            responseFail("未登录", response);
            return false;
        }
        request.setAttribute("uid", userId);
        // 如果只是校验是否登录
        if(StaticConstant.JUST_NEED_LOGIN.contains(servletPath)){
            return true;
        }

        UserPO userPO = userService.getUserById(userId);
        // 超级用户
        if(userPO.getId() == 0){
            return true;
        }

        // 校验权限
        List<String> paths = resourceService.getPathsByRoleId(userPO.getRoleId());
        if(paths.contains(servletPath)){
            return true;
        }
        responseFail("没有权限[" + servletPath + "]", response);
        return false;
    }

    private void responseFail(String message, HttpServletResponse response){
        JsonResult jsonResult = JsonResult.fail(message);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try(PrintWriter out=response.getWriter()){
            out.append(jsonResult.json());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        resourceService = applicationContext.getBean(IResourceService.class);
        userService = applicationContext.getBean(IUserService.class);
    }
}
