package com.lebron.springmvc.exception;

//系统自定义服务异常处理类,针对预期的异常，需要在程序中抛出此类的异常  
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // 异常信息
    private String message;

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
