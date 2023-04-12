package com.reine.imagehost.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author reine
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageWithUrl extends Image {

    private String url;

}
