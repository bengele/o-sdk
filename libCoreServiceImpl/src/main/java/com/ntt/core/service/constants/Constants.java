package com.ntt.core.service.constants;

import android.os.Environment;

public class Constants {
    //设备信息缓存文件路径
    public static final String DEVICE_FILE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/"+"ntt-cs.txt";

    public static final String AUTH_KEY = "auth_key_";
}
