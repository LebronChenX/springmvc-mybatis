package com.lebron.springmvc.web.websocket;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class PrincipalWithParam implements Principal {

    private final HttpSession session;
    private final HttpServletRequest request;
    private final Map<String, String> param;

    public PrincipalWithParam(HttpSession session, HttpServletRequest request, Map<String, String> param) {
        super();
        this.session = session;
        this.request = request;
        this.param = param;
    }

    public HttpSession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public Map<String, String> getParam() {
        return param;
    }

    @Override
    public String getName() {
        return ""; // whatever is appropriate for your app, e.g., user ID
    }
}
