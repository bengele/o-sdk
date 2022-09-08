package com.ntt.core.service;

import static com.ntt.core.service.supports.provider.DContentResolver.PROVIDER_URI_CODE_DEVICE_ID;
import static com.ntt.core.service.supports.provider.DContentResolver.PROVIDER_URI_CODE_PLATFORM;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.ntt.core.service.plugins.sharePreference.SharePreferenceManager;
import com.ntt.core.service.supports.auth.Auth;
import com.ntt.core.service.supports.log.XLog;
import com.ntt.core.service.supports.provider.DContentResolver;

public class CoreServiceImpl {
    private static final String TAG = CoreServiceImpl.class.getSimpleName();
    private static volatile CoreServiceImpl instance = null;

    private String mAppId;
    private Context mContext;
    private boolean isServiceConnected;

    private ICoreService mService;
    private Auth mAuth;
    private Object mLock = new Object();
    private boolean isDebug;
    private String mDomainHost;
    private String mDeviceId;

    /**
     * 初始化
     *
     * @param context   上下文
     * @param appId     应用ID
     * @param appSecret 秘钥
     */
    public static void init(Context context, String appId, String appSecret) {
        init(context, appId, appSecret, false);
    }

    /**
     * 初始化
     *
     * @param context   上下文
     * @param appId     应用ID
     * @param appSecret 秘钥
     * @param debug     当正式生成包时，请关掉此参数（不填或false）
     */
    public static void init(Context context, String appId, String appSecret, boolean debug) {
        if (instance == null) {
            synchronized (CoreServiceImpl.class) {
                if (instance == null) {
                    instance = new CoreServiceImpl(context, appId, appSecret, debug);
                }
            }
        }
    }


    public static CoreServiceImpl getInstance() {
        if (instance == null) {
            throw new Error("SDK需要初始化");
        }
        return instance;
    }

    /**
     * 构造
     *
     * @param context
     * @param appId
     * @param appSecret
     */
    private CoreServiceImpl(Context context, String appId, String appSecret, boolean debug) {
        this.mContext = context;
        this.isDebug = debug;
        //初始化MMKV
        //        MMKV.initialize(context);
        SharePreferenceManager.init(context);
        //用户信息
        this.mAuth = new Auth(appId, appSecret);
        //初始化日志
        XLog.init(context, true);
        //连接服务
        connectCoreService(true);
        String platform = getPlatform();
        if (TextUtils.isEmpty(platform)) {
            Log.e("CoreService", "警告：非在牛听听平台运行此程序！！！");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    /**
     * 连接核心服务
     *
     * @param force 是否强制重连
     */
    private void connectCoreService(boolean force) {
        if (mService != null && !force) {
            return;
        }
        Intent intent = new Intent("com.ntt.coreService");
        intent.setPackage("com.ntt.core.service");
        intent.putExtra("appName", mAppId);
        boolean ret = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        XLog.d(TAG, "准备连接服务结果=" + ret);
    }

    /**
     * 连接器接口
     */
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ICoreService.Stub.asInterface(service);
            isServiceConnected = true;
            mAuth.syncToken();
            XLog.d(TAG, "CoreService连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
            XLog.d(TAG, "与核心服务断开连接,2秒后重新连接", name.getClassName());
            new Handler().postDelayed(() -> {
                //2000毫秒后重新连接
                XLog.d(TAG, "准备尝试重新连接...", name.getPackageName(), name.getClassName());
                connectCoreService(true);
            }, 2000);
        }

        @Override
        public void onNullBinding(ComponentName name) {
            XLog.d(TAG, "onNullBinding ", name.getClassName());
        }

        @Override
        public void onBindingDied(ComponentName name) {
            XLog.d(TAG, "onBindingDied ", name.getClassName());
        }
    };

    /**
     * @return
     */
    public boolean isUseDebug() {
        return isDebug;
    }


    /**
     * 获取使用的Host
     *
     * @return
     */
    public String getUseHost() {
        return isUseDebug() ? getLocalDomainHost() : getServiceDomainHost();
        //测试代码
        //        return Utils.getApp().getString(R.string.api);
    }

    /**
     * 强制更新token
     *
     * @return
     */
    public String forceRefreshToken() {
        return mAuth.getTokenFuture(true);
    }

    /**
     * 获取token
     * 非强制
     *
     * @return
     */
    public String getToken() {
        String cacheHost = getCacheHost();
        String useHost = getUseHost();
        //当前的host与缓存的不一致时，需要清理掉缓存的token信息
        if (!cacheHost.equals(getUseHost())) {
            mAuth.clearAuthCache();
        }
        //缓存当前的host
//                MMKVUtil.getInstance().encode("CacheDomainHost", useHost);
        SharePreferenceManager.encodeString("CacheDomainHost", useHost);
        String token = mAuth.getTokenFuture(false);
        //没有拿到token，强制获取
        if (TextUtils.isEmpty(token)) {
            token = mAuth.getTokenFuture(true);
        }
        return token;
    }

    /**
     * 请求头的token
     * 允许为空字符串
     *
     * @return
     */
    public String getHeadToken() {
        return mAuth.getHeadToken();
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public String getDeviceId() {
        synchronized (mLock) {
            if (!TextUtils.isEmpty(mDeviceId)) return mDeviceId;
            if (mService == null) {
                mDeviceId = DContentResolver.query(PROVIDER_URI_CODE_DEVICE_ID);
                return mDeviceId;
            }
            try {
                mDeviceId = mService.getDeviceId();
            } catch (DeadObjectException e) {
                connectCoreService(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mDeviceId;
        }
    }

    /**
     * 获取平台
     *
     * @return
     */
    public String getPlatform() {
        String platform = DContentResolver.query(PROVIDER_URI_CODE_PLATFORM);
        return platform;
    }


    /**
     * 缓存的Host
     *
     * @return
     */
    private String getCacheHost() {
        //        return MMKVUtil.getInstance().decodeString("CacheDomainHost", "");
        return SharePreferenceManager.decodeString("CacheDomainHost");
    }

    /**
     * 获取本地host
     * 一般用于调试或单独的SDK接入，与当前设备的服务无关
     *
     * @return
     */
    private String getLocalDomainHost() {
        if (isDebug) {
            return Utils.getApp().getString(R.string.ntt_api_demo);
        }
        return Utils.getApp().getString(R.string.ntt_api);
    }

    /**
     * 获取设备已经启动服务后域名
     * 正式接入设备后，设备全局服务已经启动
     *
     * @return
     */
    private String getServiceDomainHost() {
        synchronized (mLock) {
            if (!TextUtils.isEmpty(mDomainHost)) return mDomainHost;
            if (mService == null) {
                mDomainHost = DContentResolver.query(DContentResolver.PROVIDER_URI_CODE_DOMAIN_HOST);
                XLog.d(TAG, "通过ContentProvider获取的Host", mDomainHost);
                return mDomainHost;
            }
            try {
                mDomainHost = mService.getDomainHost();
            } catch (DeadObjectException e) {
                connectCoreService(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            XLog.d(TAG, "通过Service获取的Host", mDomainHost);
            return mDomainHost;
        }
    }

}
