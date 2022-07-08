package com.ntt.core.service.supports.auth;

import static com.blankj.utilcode.util.ThreadUtils.isMainThread;
import static com.ntt.core.service.constants.Constants.AUTH_KEY;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.ntt.core.service.CoreServiceImpl;
import com.ntt.core.service.api.ApiManager;
import com.ntt.core.service.entities.SAuthEntity;
import com.ntt.core.service.plugins.http.BaseObserver;
import com.ntt.core.service.plugins.http.exception.HttpException;
import com.ntt.core.service.plugins.sharePreference.SharePreferenceManager;
import com.ntt.core.service.plugins.utils.UtAlgorithm;
import com.ntt.core.service.plugins.utils.UtDateFormat;
import com.ntt.core.service.supports.log.XLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Auth {
    private static final String TAG = Auth.class.getSimpleName();

    private String mAppId;
    private String mAppSecret;

    private String mmkvAuthKey;
    private SAuthEntity mAuthEntity;

    public Auth(String appId, String appSecret) {
        if (appId == null || TextUtils.isEmpty(appId) || appSecret == null || TextUtils.isEmpty(appSecret)) {
            throw new Error("请检查appId或appSecret的内容");
        }
        this.mAppId = appId;
        this.mAppSecret = appSecret;
        this.mmkvAuthKey = AUTH_KEY + mAppId;
        //        String authStr = MMKVUtil.getInstance().decodeString(mmkvAuthKey, "");
        String authStr = SharePreferenceManager.decodeString(mmkvAuthKey);
        mAuthEntity = GsonUtils.fromJson(authStr, SAuthEntity.class);
    }


    /**
     * 获取token
     *
     * @param force
     * @return
     */
    public String getTokenFuture(boolean force) {
        try {
            FutureTask<String> task = new FutureTask<>(() -> getToken(force));
            new Thread(task).start();
            return task.get(4000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 同步获取token
     *
     * @param force 是否强制升级
     * @return
     */
    private String getToken(boolean force) {
        //非强制并且没有缓存直接返回空字符串
        if (!force && mAuthEntity == null) return "";
        //非强制、有缓存、有效期内直接返回token
        if (!force && isValid(mAuthEntity)) {
            String token = mAuthEntity.getAccessToken();
            return token;
        }
        //异步转同步返回
        final Object o = new Object();
        final String[] token = new String[1];
        synchronized (o) {
            asyncTokenRequest(new IAuthCallback() {
                @Override
                public void onSuccess(String t) {
                    synchronized (o) {
                        Log.d(TAG, "getToken isMainThread: " + isMainThread());
                        token[0] = t;
                        o.notify();
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    synchronized (o) {
                        o.notify();
                    }
                }
            });
            try {
                if (token[0] == null) o.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return token[0];
    }

    /**
     * 同步token
     */
    public void syncToken() {
        //有缓存、有效期内直接返回,不处理
        if (isValid(mAuthEntity)) {
            return;
        }
        XLog.d(TAG, "准备异步请求Token");
        asyncTokenRequest(null);
    }

    /**
     * 头部token
     *
     * @return
     */
    public String getHeadToken() {
        if (!isValid(mAuthEntity)) return "";
        return mAuthEntity.getAccessToken();
    }

    /**
     * 清除缓存的Auth
     */
    public void clearAuthCache() {
        mAuthEntity = null;
        //        MMKVUtil.getInstance().removeValueForKey(mmkvAuthKey);
        //        String s = MMKVUtil.getInstance().decodeString(mmkvAuthKey, "");
        //        XLog.d(TAG, "已清除当前的缓存", s);
        SharePreferenceManager.removeString(mmkvAuthKey);
    }


    /**
     * 请求token
     */
    private void asyncTokenRequest(IAuthCallback callback) {
        long ts = ApiManager.getInstance().serverDateRequest();
        AuthParams params = getAuthParams(ts);
        //token请求
        ApiManager.getInstance().authRequest("application/json", params.getAuthorization(), ts, params.getBody(), new BaseObserver<SAuthEntity>() {
            @Override
            public void onSuccess(SAuthEntity data) {
                try {
                    data.setTs(ts);
                    mAuthEntity = data;
                    String authStr = GsonUtils.toJson(data);
                    //                    if (!TextUtils.isEmpty(authStr)) MMKVUtil.getInstance().encode(mmkvAuthKey, authStr);
                    if (!TextUtils.isEmpty(authStr)) SharePreferenceManager.encodeString(mmkvAuthKey, authStr);
                    XLog.d(TAG, "获取token成功", authStr);
                    if (callback != null) callback.onSuccess(data.getAccessToken());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) callback.onFailed(-1, "数据解析失败");
                }
            }

            @Override
            public void onFailure(HttpException apiException) {
                int code = apiException.getCode();
                String msg = apiException.getMessage();
                XLog.d(TAG, "获取token失败", code, msg);
                if (callback != null) callback.onFailed(code, msg);
            }
        });
    }

    /**
     * Auth组合参数
     *
     * @param ts
     * @return
     */
    private AuthParams getAuthParams(long ts) {
        Map<String, Object> body = new HashMap<>();
        String grantType = "client_credentials";
        String[] scope = new String[]{"device_" + CoreServiceImpl.getInstance().getDeviceId()};
        String scopeStr = String.join(",", scope);
        String st = UtDateFormat.formatYYYYMMDD(ts);
        String timeSt = UtDateFormat.formatYYYYMMDD(System.currentTimeMillis());
        XLog.d("getAuthParams", "st = " + st, "ts = " + ts, "system time = " + System.currentTimeMillis(), "timeStr = " + timeSt);
        String clientId = mAppId;
        String authorization = UtAlgorithm.getDeviceSign(mAppSecret, scopeStr, grantType, String.format("%s", ts));

        String ba = "Bearer " + authorization;
        body.put("scope", scope);
        body.put("grant_type", grantType);
        body.put("client_id", clientId);

        XLog.d("getOauthParams", ba, scope, grantType, clientId);
        return new AuthParams(ba, ts, body);
    }

    /**
     * 是否有效
     *
     * @param auth
     * @return
     */
    private boolean isValid(SAuthEntity auth) {
        if (auth == null) return false;
        long diff = UtDateFormat.getTimeStampSeconds() - auth.getTs();
        if (diff < auth.getExpiresIn()) {
            XLog.d("token未过期，不需要刷新:" + auth.getAccessToken());
            return true;
        }
        return false;
    }

    /**
     * 内部类实例
     */
    private static class AuthParams {
        private long ts;
        private String authorization;
        private Map<String, Object> body;

        public AuthParams(String authorization, long ts, Map<String, Object> body) {
            this.authorization = authorization;
            this.ts = ts;
            this.body = body;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public String getAuthorization() {
            return authorization;
        }

        public void setAuthorization(String authorization) {
            this.authorization = authorization;
        }

        public Map<String, Object> getBody() {
            return body;
        }

        public void setBody(Map<String, Object> body) {
            this.body = body;
        }
    }
}
