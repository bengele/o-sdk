package com.ntt.core.service.plugins.http;

public class HttpCode {

    public static final int CODE_BAD = 100;
    /*HTTP状态码*/
    public static final int CODE_SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;  //token无效
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int CONFLICT = 409;
    public static final int PRECONDITION_FAILED = 412;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;



    /*未知错误*/
    public static final int UNKNOWN_ERROR = 10000;

    /*解析错误*/
    public static final int PARSE_ERROR = 10001;

    /*网络错误*/
    public static final int NETWORK_ERROR = 10002;

    /*http错误*/
    public static final int HTTP_ERROR = 10003;

    /*SSL错误*/
    public static final int SSL_ERROR = 10005;

    /*连接超时错误*/
    public static final int TIMEOUT_ERROR = 10006;

    /*接收被调用方法内部未被捕获的异常*/
    public static final int INVOCATION_TARGET_EXCEPTION = 10007;

}
