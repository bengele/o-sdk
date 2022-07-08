package com.ntt.core.service.plugins.http;

import com.google.gson.annotations.SerializedName;

/**
 * 封装服务器Http返回的结果（服务器协定结构）
 */
public class BaseResp<T>{

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private T data;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess(){
        return code == HttpCode.CODE_SUCCESS;
    }

    public static <T> BaseResp<T> newSuccessBaseResp(T data){
        BaseResp<T> baseResp = new BaseResp<T>();
        baseResp.setCode(HttpCode.CODE_SUCCESS);
        baseResp.setData(data);
        return baseResp;
    }
}
