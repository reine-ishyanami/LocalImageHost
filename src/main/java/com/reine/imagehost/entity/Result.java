package com.reine.imagehost.entity;

import lombok.Data;

import java.util.Map;

/**
 * 返回结果类
 *
 * @author reine
 * @since 2022/4/30 8:44
 */
@Data
public class Result {

    private Result() {
    }

    private Boolean success;

    private String message;

    private Map<String, String> resultMap;

    public static Result ok(String message, Map<String, String> map) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage(message);
        result.setResultMap(map);
        return result;
    }

    public static Result ok(String message) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }

    public static Result fail(String message) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

}
