package com.reine.filebed.service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author reine
 * 2022/6/30 9:23
 */
public interface FileService {
    /**
     * 文件上传
     *
     * @param project 项目名
     * @param imgFile 文件
     * @return 文件访问地址
     */
    String storeImage(String project, File imgFile);
    /**
     * 文件上传
     *
     * @param project 项目名
     * @param imgFile 文件
     * @param fileName 文件名
     * @return 文件访问地址
     */
    String storeImage(String project, File imgFile,String fileName);

    /**
     * 读取图片
     *
     * @param project  项目名
     * @param imgName  图片名称
     * @param response 客户端响应
     * @return 是否成功
     */
    boolean showImage(String project, String imgName, HttpServletResponse response);

    /**
     * 删除图片
     *
     * @param project 项目名
     * @param imgName 图片名
     * @return 是否成功
     */
    boolean deleteImage( String project, String imgName);
}
