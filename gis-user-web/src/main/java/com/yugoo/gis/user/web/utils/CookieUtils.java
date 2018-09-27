package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.common.utils.DesUtils;
import com.yugoo.gis.pojo.po.UserPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by nihao on 18/5/8.
 */
public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public static final UserPO getUser(HttpServletRequest request){
        String token = request.getHeader("X-Token");
        if(token != null){
            return parse(token);
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(StaticConstant.cookieName.equals(cookie.getName())){
                    String value = cookie.getValue();
                    return parse(value);
                }
            }
        }
        return null;
    }

    private static UserPO parse(String token){
        try {
            String[] strings = token.split("&_");
            Integer userId = Integer.parseInt(strings[1]);
            Integer role = Integer.parseInt(strings[2]);
            UserPO userPO = new UserPO();
            userPO.setId(userId);
            userPO.setRole(role);
            return userPO;
        } catch (Exception e) {
            logger.warn("cookie 解析错误, token is {}", token);
            return null;
        }
    }

}
