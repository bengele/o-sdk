package com.ntt.core.service.api;

import com.ntt.core.service.api.entities.DateEntity;
import com.ntt.core.service.entities.SAuthEntity;
import com.ntt.core.service.plugins.http.BaseObserver;
import com.ntt.core.service.plugins.http.HTTPManager;
import com.ntt.core.service.supports.log.XLog;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class ApiManager {
    private static volatile ApiManager instance;

    private HTTPManager mHm;

    private Object mLock = new Object();

    private long mServerTime;

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }

    private ApiManager() {
        mHm = HTTPManager.getInstance();
    }

    /**
     * 获取系统时间
     */
    public long serverDateRequest() {
        synchronized (mLock) {
            mHm.obtainService(ApiService.class)
                    .getServerDate()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<DateEntity>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull DateEntity dateEntity) {
                            XLog.d("serverDateRequest", dateEntity.getDate());
                            mServerTime = dateEntity.getDate();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            synchronized (mLock) {
                                mLock.notify();
                            }
                        }

                        @Override
                        public void onComplete() {
                            synchronized (mLock) {
                                mLock.notify();
                            }
                        }
                    });
            try {
                mLock.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mServerTime;
    }

    /**
     * 获取token请求
     *
     * @param resp
     */
    public void authRequest(String cType, String authStr, long ts, Map<String, Object> body, BaseObserver<SAuthEntity> resp) {
        synchronized (mLock) {
            mHm.obtainService(ApiService.class)
                    .getDeviceToken(cType, authStr, ts, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(resp);
        }
    }


}
