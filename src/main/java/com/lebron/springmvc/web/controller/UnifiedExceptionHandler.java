package com.lebron.springmvc.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.lebron.springmvc.exception.ServiceException;

@ControllerAdvice
public class UnifiedExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedExceptionHandler.class);

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public Object noHandlerFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException exception) {
        if (isAjaxRequest(request)) {
            return ResponseEntity.status(399).body(null);
        }
        String url = "";
        return "redirect:" + url;
    }

    @ExceptionHandler(BindException.class)
    public Object handleValidationException(BindException e) {
        logger.error("Validation Exception", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("validation_exception:" + e.toString());
    }

    /**
     * 全局异常处理
     */
    @ExceptionHandler(Exception.class)
    public Object unitaryExceptionHandler(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception ex) {
        boolean isAjax = isAjaxRequest(request);
        if (ex instanceof ServiceException) {
            // 业务异常
            if (isAjax) {
                logger.error(String.format("ajax service error interface [%s], function:%s.%s, message:[%s]", request.getRequestURI(), handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(), ex.getMessage()));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
            }
        } else {
            // 系统异常
            if (isAjax) {
                logger.error(String.format("ajax system error interface [%s], function:%s.%s, message:[%s]", request.getRequestURI(), handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(), ex.getMessage()));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
            }
        }
        logger.error(String.format("url error interface [%s], function:%s.%s, message:[%s]", request.getRequestURI(), handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(), ex.getMessage()));
        // 前后端分离，直接将返回值返回给前端，跳转页面由前端自己跳转
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        // ModelAndView modelAndView = new ModelAndView();
        // modelAndView.addObject("message", ex.getMessage());
        // modelAndView.setViewName("error");
        // return modelAndView;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            return true;
        } else {
            return false;
        }
    }

}
