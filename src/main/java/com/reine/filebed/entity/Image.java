package com.reine.filebed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author reine
 * 2022/7/6 21:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
