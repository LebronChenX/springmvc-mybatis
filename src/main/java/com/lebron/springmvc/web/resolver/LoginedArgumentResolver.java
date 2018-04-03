package com.lebron.springmvc.web.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.lebron.springmvc.annotation.Logined;
import com.lebron.springmvc.model.User;

public class LoginedArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 如果该参数注解有@Logined
        if (parameter.getParameterAnnotation(Logined.class) != null && parameter.getParameterType() == User.class) {
            // 支持解析该参数
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        // 这里暂时把User对象放在session中
        User user = (User) request.getSession().getAttribute("USER_NAME");
        return user;
    }

}
