package com.yugoo.gis.user.web.handler;

import com.yugoo.gis.user.web.result.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nihao on 16/10/22.
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj, Exception e) {
        if (obj instanceof HandlerMethod) {
            logger.error("\r\n请求路径:{},类:{},方法:{},错误信息:{}",
                    httpServletRequest.getServletPath(),
                    ((HandlerMethod)obj).getBean().getClass().getName(),
                    ((HandlerMethod)obj).getMethod().getName(),
                    e.getMessage(),
                    e);
        }
        else {
            logger.error("\r\n请求路径:{},错误信息:{}",
                    httpServletRequest.getServletPath(),
                    e.getMessage(),
                    e);
        }

        String message = e.getClass().getName()+" "+e.getMessage();
        if (httpServletRequest.getServletPath().endsWith(".html")) {
            httpServletRequest.setAttribute("errorMsg", message);
            try {
                httpServletRequest.getRequestDispatcher("/500.html").forward(httpServletRequest, httpServletResponse);
            } catch (ServletException e1) {
                logger.error(e1.getMessage(),e1);
            } catch (IOException e1) {
                logger.error(e1.getMessage(),e1);
            }
        }
        else {
            JsonResult jsonResult = JsonResult.fail(message);
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json; charset=utf-8");
            try(PrintWriter out=httpServletResponse.getWriter()){
                out.append(jsonResult.json());
            }catch (Exception e1){
                logger.error(e1.getMessage(),e1);
            }
        }
        return null;
    }
}
