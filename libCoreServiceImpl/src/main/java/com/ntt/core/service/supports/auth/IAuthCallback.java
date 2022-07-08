package com.ntt.core.service.supports.auth;

public interface IAuthCallback {
    void onSuccess(String token);
    void onFailed(int code,String msg);
}
