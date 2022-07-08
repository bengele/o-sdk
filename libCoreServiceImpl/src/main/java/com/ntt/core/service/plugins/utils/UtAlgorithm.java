package com.ntt.core.service.plugins.utils;



import org.apache.ccbase64.digest.DigestUtils;
import org.apache.ccbase64.digest.HmacAlgorithms;
import org.apache.ccbase64.digest.HmacUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 一些通用的算法工具类
 */
public class UtAlgorithm {

    /**
     * 设备签名
     * @param scope
     * @param grantType
     * @param timestamp
     * @return
     */
    public static String getDeviceSign(String secret,String scope,String grantType,String timestamp){
        String contentToSignature = String.format("scope=%s&grant_type=%s&timestamp=%s", scope, grantType, timestamp);
        String hmacHex = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, secret).hmacHex(contentToSignature);
        String sign = DigestUtils.md5Hex(hmacHex);
        return sign;
    }


    public static String getFormatMD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //将字符串转换成字符串数组
        char[] pswdArray = str.toCharArray();
        byte[] pswdByte = new byte[pswdArray.length];
        //将字符转换成字节
        for (int i = 0; i < pswdArray.length; i++) {
            pswdByte[i] = (byte) pswdArray[i];
        }
        byte[] digest = md5.digest(pswdByte);
        //将得到的字节数组转换成十六进制数
        StringBuilder buff = new StringBuilder();
        for (byte aDigest : digest) {
            int num = ((int) aDigest) & 0xff;
            //如果不足16，加0填充
            if (num < 16)
                buff.append("0");
            buff.append(Integer.toHexString(num));
        }
        return buff.toString();
    }


    public static String hmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes("UTF-8");
        // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
        // 生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance("HmacSHA1");
        // 用给定密钥初始化 Mac 对象
        mac.init(secretKey);
        byte[] text = encryptText.getBytes("UTF-8");
        // 完成 Mac 操作
        byte[] digest = mac.doFinal(text);
        StringBuilder stringBuilder = bytesToHexString(digest);
        if (stringBuilder == null) {
            return "";
        }
        return stringBuilder.toString();
    }

    private static StringBuilder bytesToHexString(byte[] bytesArray) {
        if (bytesArray == null) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (byte b : bytesArray) {
            String hv = String.format("%02x", b);
            sBuilder.append(hv);
        }
        return sBuilder;
    }


    public static String getSHA1(String val) {
        MessageDigest msgDig = null;
        try {
            msgDig = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder buf = new StringBuilder();
        if (msgDig != null) {
            msgDig.update(val.getBytes());
            byte[] msg = msgDig.digest();

            for (byte b : msg) {
                int halfbyte = (b >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    buf.append(halfbyte <= 9 ? (char) ('0' + halfbyte) : (char) ('a' + halfbyte - 10));
                    halfbyte = b & 0x0F;
                } while (two_halfs++ < 1);
            }
        }
        return buf.toString();
    }
}
