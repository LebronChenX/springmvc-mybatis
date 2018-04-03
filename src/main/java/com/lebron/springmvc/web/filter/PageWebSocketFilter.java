package com.lebron.springmvc.web.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.lebron.springmvc.web.websocket.PrincipalWithParam;

@WebFilter("/pagewebsocket")
public class PageWebSocketFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession httpSession = httpRequest.getSession();
        httpSession.setMaxInactiveInterval(0);
        String identifyId = (String) request.getParameter("identifyId");
        String pid = (String) request.getParameter("pid");

        Map<String, String> param = new HashMap<String, String>();
        param.put("identifyId", identifyId);
        param.put("pid", pid);
        final PrincipalWithParam p = new PrincipalWithParam(httpSession, httpRequest, param);
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {

            @Override
            public Principal getUserPrincipal() {
                return p;
            }
        };
        chain.doFilter(wrappedRequest, response);
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }
}
