package com.reine.imagehost.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author reine
 * 2022/7/6 21:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Image extends Img {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 文件路径
     */
    private String path;
}
