package com.ntt.core.service.plugins.http.exception;

/**
 * 自定义服务器异常
 */
public class ServerException extends RuntimeException {

    //错误码
    private int code;
    //错误描述
    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
