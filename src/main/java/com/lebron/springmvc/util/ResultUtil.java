package com.lebron.springmvc.util;

@SuppressWarnings("all")
public class ResultUtil {

    private static final int SUCCESS = 0;
    private static final int NO_LOGIN = 1;

    public static Result success() {
        return success(null);
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setMsg("success");
        result.setData(object);
        return result;
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result nologin() {
        Result result = new Result();
        result.setCode(NO_LOGIN);
        result.setMsg("No login");
        return result;
    }
}
