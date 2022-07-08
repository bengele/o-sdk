package com.ntt.core.service.plugins.http.interceptor;

import android.text.TextUtils;

import com.ntt.core.service.CoreServiceImpl;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHeadInterceptor implements Interceptor {

    private static final String USER_AGENT = "NTT-SDK-";   //user-agent
    private static final String ACCEPT = "application/json";    //accept


    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl originalUrl = request.url();

        String deviceId = CoreServiceImpl.getInstance().getDeviceId();
        HttpUrl url = originalUrl.newBuilder()
                .addQueryParameter("deviceId", deviceId)
                .build();
        Request.Builder builder = request.newBuilder().url(url);
        //
        builder.addHeader("User-Agent", USER_AGENT);
        builder.addHeader("Accept", ACCEPT);
        builder.addHeader("module", "CoreService");
        builder.addHeader("NttDevice", CoreServiceImpl.getInstance().getPlatform());

        String authToken = "Bearer " + CoreServiceImpl.getInstance().getHeadToken();
        if (request.header("Authorization") == null) {
            if (!TextUtils.isEmpty(authToken)) builder.addHeader("Authorization", authToken);
        }
        return chain.proceed(builder.build());
    }
}
