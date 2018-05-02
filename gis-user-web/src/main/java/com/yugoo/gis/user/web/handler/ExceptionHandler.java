package com.yugoo.gis.user.web.handler;

import com.yugoo.gis.user.web.result.JsonResult;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by nihao on 16/10/22.
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        logger.error("\r\n请求路径:{},类:{},方法:{},错误信息:{}",
                httpServletRequest.getServletPath(),
                ((HandlerMethod)o).getBean().getClass().getName(),
                ((HandlerMethod)o).getMethod().getName(),
                e.getMessage(),
                e);
        if("XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"))){
            JsonResult jsonResult = JsonResult.fail(e.getClass().getName()+" "+e.getMessage());
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json; charset=utf-8");
            try(PrintWriter out=httpServletResponse.getWriter()){
                out.append(jsonResult.json());
            }catch (Exception e1){
                logger.error(e1.getMessage(),e1);
            }
            return null;
        }
        else{
            Map<String,Object> map= Maps.newHashMap();
            map.put("mess",e.getClass().getName()+" "+e.getMessage());
            return new ModelAndView("error/error",map);
        }
    }
}
