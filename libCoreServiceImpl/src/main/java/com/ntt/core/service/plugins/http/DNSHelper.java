package com.ntt.core.service.plugins.http;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.tencent.msdk.dns.DnsConfig;
import com.tencent.msdk.dns.MSDKDnsResolver;


import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

@Keep
public class DNSHelper {

    private static final String TENCENTYUN_DNS_KEY = "Y1qD31or";
    private static final String TENCENTYUN_APP_ID = "0AND06FZ8C4ETLIT";
    private static final String TENCENTYUN_DNS_ID = "2750";

    public static void init(Context context) {
        LogUtils.d("DNSHelper 开始初始化DNSHelper");
        DnsConfig config = new DnsConfig.Builder()
                .appId(TENCENTYUN_APP_ID)
                .dnsId(TENCENTYUN_DNS_ID)
                .dnsKey(TENCENTYUN_DNS_KEY)
                .dnsIp("119.29.29.99")
                .https()
                .token("635945393")
                .logLevel(Log.VERBOSE)
                .preLookupDomains(
                        "gw.benewtech.cn",
                        "gw-demo.benewtech.cn"
                )
                .persistentCacheDomains(
                        "gw.benewtech.cn",
                        "gw-demo.benewtech.cn"
                )
                .setCustomNetStack(3)
                .timeoutMills(2000)
                .build();

        MSDKDnsResolver.getInstance().init(context, config);
    }

    public static final Dns dns = hostname -> {
        List<InetAddress> toMutableList = null;
        try {
            long startTime = System.currentTimeMillis();
            toMutableList = Arrays.asList(DnsServiceWrapper.getAddrsByName(hostname));
            LogUtils.d(
                    "OkHttpHelper DNS时间:" + (System.currentTimeMillis() - startTime),
                    "DNS列表=" + GsonUtils.toJson(toMutableList)
            );
        } catch (Exception e) {
            LogUtils.d("DNSHelper 解析失败", e.getMessage());
        }
        if (toMutableList == null || toMutableList.isEmpty()) {
            toMutableList = Dns.SYSTEM.lookup(hostname);
        }
        return toMutableList;
    };

}
