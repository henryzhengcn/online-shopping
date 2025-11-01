package com.onlineshopping.common.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @description ResponseResult to encapsulate the return code, message and data
 * @author henryzheng
 * @date  2025/10/28
 */
@Data
public class ResponseResult<T> implements Serializable {

    private int code;
    private String message;
    private Object data;

    public static <T> ResponseResult<T> ok() {
        return ok(HttpStatus.OK.value(), "success", null);
    }

    public static <T> ResponseResult<T> ok(T data) {
        return ok(HttpStatus.OK.value(), "success", data);
    }

    public static <T> ResponseResult<T> ok(int code, T data) {
        return ok(code, "success", data);
    }

    public static <T> ResponseResult<T> ok(int code, String message, T data) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.code = code;
        result.message = message;
        result.data = data;
        return result;
    }

    public static <T> ResponseResult<T> fail(String message) {
        return ok(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <T> ResponseResult<T> fail(int code, String message) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.code = code;
        result.message = message;
        return result;
    }

}
