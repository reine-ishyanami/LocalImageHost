package com.reine.imagehost.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 返回结果类
 *
 * @author reine
 * @since 2022/4/30 8:44
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class Result<T> {

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "错误提示信息")
    private String message;

    @Schema(description = "响应数据内容")
    private T data;

    public static <T> Result<T> ok(String message, T data) {
        return new Result<T>()
                .setSuccess(true)
                .setMessage(message)
                .setData(data);
    }

    public static <T> Result<T> ok(String message) {
        return new Result<T>()
                .setSuccess(true)
                .setMessage(message);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<T>()
                .setSuccess(false)
                .setMessage(message);
    }

}
