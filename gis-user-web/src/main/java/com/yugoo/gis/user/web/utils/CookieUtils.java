package com.yugoo.gis.user.web.utils;

import com.yugoo.gis.common.constant.StaticConstant;
import com.yugoo.gis.common.utils.DesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by nihao on 18/5/8.
 */
public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public static final Integer getUserId(HttpServletRequest request){
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

    private static Integer parse(String token){
        try {
            Integer userId = Integer.parseInt(DesUtils.decrypt(token.split("_")[1]));
            return userId;
        } catch (Exception e) {
            logger.warn("cookie 解析错误, token is {}", token);
            return null;
        }
    }

}
