/*
package com.itheima.exception;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomExceptionHandler implements HandlerExceptionResolver {

    @Nullable
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        e.printStackTrace();

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error.do");
        if (e instanceof CustomException) {
            //如果是自定义异常,就返回Result结果
            CustomException customException = (CustomException) e;

        }
        return null;
    }
}
*/
