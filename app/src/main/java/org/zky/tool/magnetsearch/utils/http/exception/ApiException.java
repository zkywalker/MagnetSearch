package org.zky.tool.magnetsearch.utils.http.exception;

/**
 * 被捕获的异常
 * Created by zhangkun on 2017/7/27.
 */

public class ApiException extends Exception {
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }

    @Override
    public String getMessage() {
        return message;
    }
}