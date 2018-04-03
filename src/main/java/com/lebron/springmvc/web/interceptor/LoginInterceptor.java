package com.lebron.springmvc.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object obj, Exception exception) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object obj, ModelAndView arg3) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {

        return true;
    }

}
