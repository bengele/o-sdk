package com.ntt.core.service.plugins.http.exception;

import android.content.Context;


public class ExceptionHandlerDelegate {

    private Context mContext;
    private ResponseExceptionListener mResponseExceptionListener;

    public ExceptionHandlerDelegate(Context context, ResponseExceptionListener responseExceptionListener){
        this.mContext = context;
        this.mResponseExceptionListener = responseExceptionListener;
    }

    /**
     * 处理异常
     * @param throwable
     */
    public HttpException handleException(Throwable throwable){
        return mResponseExceptionListener.handleException(mContext,throwable);
    }
}
