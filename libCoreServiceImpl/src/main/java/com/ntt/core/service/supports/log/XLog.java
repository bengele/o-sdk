package com.ntt.core.service.supports.log;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;

public class XLog {
    private static final long FILE_SIZE = 1024 * 1024;  //默认存储日志大小
    private static final String SDF_LOG_TIME = "yyyyMMdd"; //日志文件名日期格式

    private static boolean mDebug = false;
    private static Context mContext;


    /**
     * 初始化
     *
     * @param context
     * @param debug
     */
    public static void init(Context context, boolean debug) {
        mContext = context;
        mDebug = debug;
    }

    public static void d(final Object... contents) {
        if (mDebug) {
            LogUtils.d(contents);
        }
    }

    public static void w(final Object... contents) {
        if (mDebug) {
            LogUtils.w(contents);
        }
    }

    public static void i(final Object... contents) {
        if (mDebug) {
            LogUtils.i(contents);
        }
    }

    public static void e(final Object... contents) {
        if (mDebug) {
            LogUtils.e(contents);
        }
    }
}
