package com.reine.imagehost.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author reine
 */
@Data
public class Img {
    /**
     * 项目名
     */
    @Schema(description = "项目名称")
    private String project;
    /**
     * 文件名
     */
    @Schema(description = "文件名称")
    private String name;
}
