// IAuthCallback.aidl
package com.ntt.core.service.classes;

// Declare any non-default types here with import statements

interface IAuthCallback {
    void onSuccess(boolean isSync,String token);
    void onFailure(String msg);
}