package com.reine.filebed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果类
 * @author reine
 * @since 2022/4/30 8:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Integer code;

    private String message;

    private String projectAndFileName;

}
