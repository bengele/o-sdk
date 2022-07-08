package com.ntt.demo;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.ntt.core.service.CoreServiceImpl;

public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            /**
             * context：上下文
             * appId
             * appSecret
             * debug : 默认为false，使用的是在线服务器的请求接口，true：使用测试服务器的接口。适用无此参数接口
             */
            CoreServiceImpl.init(this, "out-m1-family", "UeluZp5cbRNTGs3tsHpQHzIV5relaQk9");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
