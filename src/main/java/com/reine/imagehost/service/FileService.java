package com.reine.imagehost.service;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.entity.Img;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.List;

/**
 * @author reine
 * 2022/6/30 9:23
 */
public interface FileService {
    /**
     * GUI文件上传
     *
     * @param path    保存路径
     * @param project 项目名
     * @param name
     * @param imgFile 文件
     * @return 文件访问地址
     * @throws Exception 文件不存在
     */
    Img storeImageGUI(String path, String project,String name, File imgFile) throws Exception;

    /**
     * RestAPI文件上传
     *
     * @param project  项目名
     * @param imgFile  文件
     * @param fileName 文件名
     * @return 文件访问地址
     * @throws Exception 文件不存在
     */
    Img storeImageAPI(String project, File imgFile, String fileName) throws Exception;

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
     * @return 返回信息
     */
    String deleteImage(String project, String imgName);

    /**
     * 创建数据库
     */
    void createTable();

    /**
     * 获取项目下所有图片，以及其访问路径
     *
     * @param project 项目名称，为null则查询全部
     * @return 图片列表
     */
    List<ImageWithUrl> listImage(String project);

    /**
     * 根据id，project，name查询图片
     * @param id
     * @param project
     * @param name
     * @return
     */
    List<Image> queryImageList(String id, String project, String name);
}
