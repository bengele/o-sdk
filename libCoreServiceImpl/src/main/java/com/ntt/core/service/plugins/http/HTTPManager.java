package com.ntt.core.service.plugins.http;

import androidx.annotation.NonNull;


import com.ntt.core.service.CoreServiceImpl;
import com.ntt.core.service.plugins.http.interceptor.HttpHeadInterceptor;
import com.ntt.core.service.plugins.http.interceptor.HttpResponseInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPManager {
    private OkHttpClient mOkHttpClient;
    private Retrofit mRetrofit;

    private static class HTTPManagerHolder {
        private static final HTTPManager INSTANCE = new HTTPManager();
    }

    public static final HTTPManager getInstance() {
        return HTTPManagerHolder.INSTANCE;
    }

    private HTTPManager() {
        this.mOkHttpClient = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()) //RetrofitUrlManager 初始化
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .addInterceptor(new HttpHeadInterceptor())
                .addNetworkInterceptor(new HttpResponseInterceptor())
                .build();

        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(CoreServiceImpl.getInstance().getUseHost())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create())//使用Gson
                .client(mOkHttpClient)
                .build();
    }


    public <T> T obtainService(@NonNull Class<T> service) {
        return mRetrofit.create(service);
    }
}
