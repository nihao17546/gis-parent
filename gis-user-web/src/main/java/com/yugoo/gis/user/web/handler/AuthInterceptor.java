package com.yugoo.gis.user.web.handler;

import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.pojo.po.UserPO;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        logger.debug("request[{}]", servletPath);
        UserPO userPO = CookieUtils.getUser(request);
        if(userPO == null){
            responseFail("未登录", response);
            return false;
        }
        List<String> paths = StaticConstant.getPathByRole(userPO.getRole());
        if (!paths.contains(servletPath)) {
            responseFail("没有权限[" + servletPath + "]", response);
            return false;
        }
        request.setAttribute("uid", userPO.getId());
        request.setAttribute("role", userPO.getRole());
        return true;
    }

    private void responseFail(String message, HttpServletResponse response){
        JsonResult jsonResult = JsonResult.fail(message);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try(PrintWriter out = response.getWriter()){
            out.append(jsonResult.json());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    }
}
