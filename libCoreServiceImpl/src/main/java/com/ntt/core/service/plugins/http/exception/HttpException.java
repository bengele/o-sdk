package com.ntt.core.service.plugins.http.exception;


import com.ntt.core.service.plugins.http.HttpCode;

/**
 * 服务器返回的错误
 */
public class HttpException extends Exception {

    //http错误码（服务器返回的错误码）
    private int code;
    //包装错误的body
    private String message;

    public HttpException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNetworkError(){
        return code == HttpCode.NETWORK_ERROR
                || code == HttpCode.SSL_ERROR;
    }

    public boolean isTimeout(){
        return code == HttpCode.TIMEOUT_ERROR;
    }
}
