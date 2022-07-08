package com.ntt.core.service.plugins.http.exception;

import android.content.Context;


/**
 * 处理异常监听接口
 * 各组件可自定义实现
 */
public interface ResponseExceptionListener {
    /**
     * 处理异常
     * @param context
     * @param throwable
     */
    HttpException handleException(Context context, Throwable throwable);

    /**
     * 空实现
     */
    ResponseExceptionListener EMPTY = new ResponseExceptionListener() {
        @Override
        public HttpException handleException(Context context, Throwable throwable) {
            return null;
        }
    };

}
