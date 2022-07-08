package com.ntt.core.service.supports.provider;

import android.net.Uri;

import com.blankj.utilcode.util.Utils;

public class DContentResolver {
    //Author
    private static final String PROVIDER_AUTHOR = "com.ntt.core.service.Provider";
    //Path-- 设备id
    private static final String PROVIDER_URI_PATH_DEVICE_ID = "/com/ntt/core/service/deviceId";
    //Path-- 域名
    private static final String PROVIDER_URI_PATH_DOMAIN_HOST = "/com/ntt/core/service/domainHost";
    //Path-- token
    private static final String PROVIDER_URI_PATH_HOST_TOKEN = "/com/ntt/core/service/hostToken";
    //Path-- 平台
    private static final String PROVIDER_URI_PATH_PLATFORM = "/com/ntt/core/service/platform";
    //霍尔状态
    private static final String PROVIDER_URI_PATH_HALL_STATUS = "/com/ntt/core/service/platform/hall/status";

    //URI_CODE
    public static final int PROVIDER_URI_CODE_DEVICE_ID = 1001;
    public static final int PROVIDER_URI_CODE_DOMAIN_HOST = 1002;
    public static final int PROVIDER_URI_CODE_HOST_TOKEN = 10023;
    public static final int PROVIDER_URI_CODE_PLATFORM = 10024;
    public static final int PROVIDER_URI_CODE_HALL_STATUS = 10027;


    /**
     * 查询
     * @param code
     * @return
     */
    public static String query(int code){
        String uri = "content://" + PROVIDER_AUTHOR ;
        switch (code){
            case PROVIDER_URI_CODE_DEVICE_ID:
                uri += PROVIDER_URI_PATH_DEVICE_ID;
                break;
            case PROVIDER_URI_CODE_DOMAIN_HOST:
                uri += PROVIDER_URI_PATH_DOMAIN_HOST;
                break;
            case PROVIDER_URI_CODE_HOST_TOKEN:
                uri += PROVIDER_URI_PATH_HOST_TOKEN;
                break;
            case PROVIDER_URI_CODE_PLATFORM:
                uri += PROVIDER_URI_PATH_PLATFORM;
                break;
            case PROVIDER_URI_CODE_HALL_STATUS:
                uri += PROVIDER_URI_PATH_HALL_STATUS;
        }
        String result = Utils.getApp().getContentResolver().getType(Uri.parse(uri));
        return result;
    }
}
