package com.reine.imagehost.mapper;

import com.reine.imagehost.entity.Image;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author reine
 * 2022/7/6 21:34
 */
@Mapper
public interface ImgMapper {

    /**
     * 存储图片信息到数据库
     *
     * @param image 图片信息
     * @return 受影响的行数
     */
    Integer storeImg(Image image);

    /**
     * 通过文件name获取文件path信息
     *
     * @param project 项目名
     * @param name    文件名
     * @return 文件存储路径
     */
    String getPath(String project, String name);

    /**
     * 通过文件name删除文件
     *
     * @param project 项目名
     * @param name    文件名
     * @return 受影响的行数
     */
    Integer deleteImg(String project, String name);

    /**
     * 查询项目下所有图片
     *
     * @param project 项目名
     * @return 图片列表
     */
    List<Image> listImg(String project);

    /**
     * 根据id，project，name查询图片列表
     *
     * @param id
     * @param project
     * @param name
     * @return
     */
    List<Image> queryImageListByIdAndProjectAndName(String id, String project, String name);
}
