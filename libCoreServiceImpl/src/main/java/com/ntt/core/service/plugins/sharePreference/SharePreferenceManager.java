package com.ntt.core.service.plugins.sharePreference;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceManager {

    private static Context mContext;
    private static SharedPreferences mSharedPreferences;

    public static void init(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences("lib_coreservice",Context.MODE_PRIVATE);
    }

    /**
     * 存储字符串
     * @param k
     * @param s
     */
    public static void encodeString(String k,String s){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(k, s);
        editor.commit();
    }

    /**
     * 获取字符串
     * @param k
     * @return
     */
    public static String decodeString(String k){
       return mSharedPreferences.getString(k,"");
    }


    /**
     * 移除一个数据
     * @param k
     */
    public static void removeString(String k){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(k);
        editor.commit();
    }

    /**
     * 清除所有
     */
    public static void clearAll(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
