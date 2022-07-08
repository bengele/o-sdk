package com.ntt.core.service.plugins.http;

import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.ntt.core.service.plugins.http.exception.ExceptionHandlerDelegate;
import com.ntt.core.service.plugins.http.exception.HttpException;
import com.ntt.core.service.plugins.http.exception.ResponseExceptionListener;
import com.ntt.core.service.plugins.http.exception.RxExceptionHandler;
import com.ntt.core.service.plugins.http.exception.ServerException;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


public abstract class BaseObserver<T> implements Observer<BaseResp<T>> {

    private ExceptionHandlerDelegate mHandlerDelegate;

    public BaseObserver(RxExceptionHandler rxExceptionHandler) {
        mHandlerDelegate = rxExceptionHandler.getHandlerFactory();
    }


    public BaseObserver() {
        Context context = Utils.getApp();
        mHandlerDelegate = new ExceptionHandlerDelegate(context, new ResponseExceptionListener() {
            @Override
            public HttpException handleException(Context context, Throwable throwable) {
                return new HttpException(throwable,HttpCode.CODE_BAD);
            }
        });
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResp<T> tBaseResp) {
        if (tBaseResp == null) return;
        if (tBaseResp.isSuccess()) {
            onSuccess(tBaseResp.getData());
        } else {
            if (mHandlerDelegate == null) return;
            ServerException serverException = handleServerException(tBaseResp.getCode(), tBaseResp.getMessage());
            HttpException httpException = mHandlerDelegate.handleException(serverException);
            if (httpException == null) return;
            //将服务器返回的错误，给业务处理
            onFailure(httpException);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mHandlerDelegate == null) return;
        //根据需求将一些非服务器返回的错误，传递给业务处理
        HttpException apiException = mHandlerDelegate.handleException(e);
        if (apiException == null) {
            return;
        }
        onFailure(apiException);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 处理自定义的服务器异常
     *
     * @param code
     * @param message
     * @return
     */
    private ServerException handleServerException(int code, String message) {
        if (code == HttpCode.CODE_SUCCESS) return null;
        ServerException serverException = new ServerException();
        serverException.setCode(code);
        serverException.setMessage(message);
        return serverException;
    }


    /**
     * 返回成功数据
     *
     * @param data
     */
    public abstract void onSuccess(T data);

    /**
     * 返回封装的结果（与服务器约定错误类型）
     *
     * @param apiException
     */
    public abstract void onFailure(HttpException apiException);
}
