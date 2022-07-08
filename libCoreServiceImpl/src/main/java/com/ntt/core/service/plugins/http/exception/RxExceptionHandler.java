package com.ntt.core.service.plugins.http.exception;

import android.content.Context;

public class RxExceptionHandler {

    private ExceptionHandlerDelegate mHandlerFactory;

    private RxExceptionHandler(Builder builder){
        this.mHandlerFactory = builder.mExceptionHandlerDelegate;
    }

    public ExceptionHandlerDelegate getHandlerFactory(){
        return mHandlerFactory;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private Context mContext;
        private ResponseExceptionListener mResponseExceptionListener;
        private ExceptionHandlerDelegate mExceptionHandlerDelegate;

        public Builder with(Context context){
            this.mContext = context;
            return this;
        }

        public Builder responseExceptionListener(ResponseExceptionListener responseExceptionListener){
            this.mResponseExceptionListener = responseExceptionListener;
            return this;
        }

        public RxExceptionHandler build(){
            if (mContext == null) throw new IllegalStateException("Context is Null");

            if (mResponseExceptionListener == null) throw new IllegalStateException("responseExceptionListener is Null");

            this.mExceptionHandlerDelegate = new ExceptionHandlerDelegate(mContext,mResponseExceptionListener);
            return new RxExceptionHandler(this);
        }

    }
}
