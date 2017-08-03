package com.liangjing.httpconnection.http;

/**
 * Created by liangjing on 2017/8/3.
 * function:对响应状态接口的封装
 */

public enum HttpStatus {

    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    OK(200, "OK"),
    CREATED(201, "Created"),
    Accepted(202, "Accepted "),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),

    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    USE_PROXY(305, "Use Proxy "),
    UNUSED(306, "Unused"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    BAD_REQUEST(400, "Bad Request"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not_Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed "),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    URI_TOO_LONG(414, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type  Server Error"),
    FAILED(417, "Failed Server Error"),
    UPGRADE_REQUIRED(426, "Upgrade Required"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad_Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported ");

    //响应码
    private int mCode;

    //响应信息
    private String mMessage;

    private HttpStatus(int code, String message) {
        this.mCode = code;
        this.mMessage = message;
    }


    /**
     * function:判断发起的网络请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        int value = mCode / 100;
        if (value == 2) {
            return true;
        }
        return false;
    }

    /**
     * function:根据响应码来获取响应状态
     *
     * @param value
     * @return
     */
    public static HttpStatus getValues(int value) {
        //首先用foreach来遍历整个枚举类
        for (HttpStatus httpstatus : values()) {
            if (value == httpstatus.mCode) {
                return httpstatus;
            }
        }
        return null;
    }

}
