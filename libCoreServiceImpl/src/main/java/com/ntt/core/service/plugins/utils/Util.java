package com.ntt.core.service.plugins.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Util {

    /**
     * 写文件
     * @param content
     * @param path
     */
    public static void writeFile(String content, String path) {
        try {
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(path);
            output.write(content.getBytes());
            //将String字符串以字节流的形式写入到输出流中
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读文件
     * @param path
     * @return
     */
    public static String readFile(String path) {
        StringBuilder sb = new StringBuilder("");
        try {
            FileInputStream input = new FileInputStream(path);
            byte[] temp = new byte[1024];

            int len = 0;
            //读取文件内容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //关闭输入流
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
