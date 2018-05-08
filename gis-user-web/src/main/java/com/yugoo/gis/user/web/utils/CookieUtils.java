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
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(StaticConstant.cookieName.equals(cookie.getName())){
                String value = cookie.getValue();
                try {
                    Integer userId = Integer.parseInt(DesUtils.decrypt(value));
                    return userId;
                } catch (Exception e) {
                    logger.warn("cookie 解析错误, value is {}", value);
                    return null;
                }
            }
        }
        return null;
    }

}
