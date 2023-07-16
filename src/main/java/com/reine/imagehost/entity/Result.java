package com.reine.imagehost.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "错误提示信息")
    private String message;

    @Schema(description = "响应数据内容")
    private Map<String, Object> data;

    public static Result ok(String message, Map<String, Object> map) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(map);
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
