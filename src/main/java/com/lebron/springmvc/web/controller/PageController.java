package com.lebron.springmvc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = {"/index", "/" })
    public String toIndex() {
        return "index";
    }

    @RequestMapping(value = "/support")
    public String toSupport() {
        return "support";
    }

    @RequestMapping(value = "/error")
    public String toError() {
        return "error";
    }
}
