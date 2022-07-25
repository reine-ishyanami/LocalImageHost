package com.reine.imagehost.entity;

import lombok.Data;

/**
 * @author reine
 * 2022/7/6 21:40
 */
@Data
public class Image {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 项目名
     */
    private String project;
    /**
     * 文件名
     */
    private String name;
}
