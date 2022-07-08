package com.ntt.core.service.api;

import com.ntt.core.service.api.entities.DateEntity;
import com.ntt.core.service.entities.SAuthEntity;
import com.ntt.core.service.plugins.http.BaseResp;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    /**
     * 获取服务器时间
     */
    @GET("date")
    Observable<DateEntity> getServerDate();

    /**
     * 设备获取访问Token
     *
     * @param body
     * @return
     */
    @POST("oauth2/token")
    Observable<BaseResp<SAuthEntity>> getDeviceToken(@Header("Content-Type") String cType, @Header("Authorization") String authorization, @Query("ts") long ts, @Body Map<String, Object> body);

}
