package com.reine.imagehost.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author reine
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageWithUrl extends Image {

    /**
     * 访问url
     */
    @Schema(description = "访问url")
    private String url;
}
