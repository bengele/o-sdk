package com.ntt.demo.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.FileUtils;
import com.ntt.core.service.CoreServiceImpl;
import com.ntt.demo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView deviceIdTv;
    TextView hostTv;
    TextView tokenTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceIdTv = findViewById(R.id.deviceId);
        hostTv = findViewById(R.id.host);
        tokenTv = findViewById(R.id.token);
    }

    public void testClick(View view) {
        long t = System.currentTimeMillis();
        String host = CoreServiceImpl.getInstance().getUseHost();
        String deviceId = CoreServiceImpl.getInstance().getDeviceId();
        String token = CoreServiceImpl.getInstance().forceRefreshToken();
        String token1 = CoreServiceImpl.getInstance().getToken();
        long diff = System.currentTimeMillis() - t;
        Log.d("testClick","耗时 = "+diff);
        deviceIdTv.setText(deviceId);
        hostTv.setText(host);
        tokenTv.setText(token);

        String platform = CoreServiceImpl.getInstance().getPlatform();
        Log.d("", platform);


    }

    /**
     * 音频播放
     */
    private void openAudioPlayer() {



    }


    private void openDomainHost() {
        //本地接口调试时，debug为true
        CoreServiceImpl.init(this, "", "", true);
        String host = CoreServiceImpl.getInstance().getUseHost();


        //与设备的固件调试时（设备固件的服务已经没问题的情况下）或需要正式上线
        CoreServiceImpl.init(this, "", "");
        host = CoreServiceImpl.getInstance().getUseHost();

    }


    private void openToken() {
        //直接用这个获取token，token的生命周期sdk内部会维护一轮
        String token = CoreServiceImpl.getInstance().getToken();
        //强制刷新token,业务有需要的时候强制刷新token
        token = CoreServiceImpl.getInstance().forceRefreshToken();
    }

    private void openDeviceId() {
        //获取设备ID，设备id严重依赖于设备的固件，如在调试中需要使用设备id时，请先使用假的设备ID和token
        String deviceId = CoreServiceImpl.getInstance().getDeviceId();
    }
}
