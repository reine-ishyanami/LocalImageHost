package com.reine.imagehost.service;

import cn.hutool.core.io.FileTypeUtil;
import com.reine.imagehost.entity.Image;
import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.entity.Img;
import com.reine.imagehost.mapper.ImgMapper;
import com.reine.imagehost.utils.AsyncTask;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象服务，封装多个服务之间相同的操作
 *
 * @author reine
 * 2024/3/6 12:17
 */
@Component
@Slf4j
public abstract class AbstractFileService implements FileService {

    @Value("${server.port}")
    private Integer port;

    @Value("${web.base.path.image}")
    private String webBasePath;

    protected final ImgMapper imgMapper;


    protected final AsyncTask task;

    public AbstractFileService(ImgMapper imgMapper, AsyncTask task) {
        this.imgMapper = imgMapper;
        this.task = task;
    }

    @Override
    public boolean showImage(String project, String imgName, HttpServletResponse response) {
        String filePath = getPath(project, imgName);
        try {
            @Cleanup FileInputStream fileInputStream = new FileInputStream(filePath);
            // 使用ByteArrayInputStream包装，以实现reset()操作
            @Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream.readAllBytes());
            String file_type = FileTypeUtil.getType(inputStream);
            switch (file_type) {
                case "gif", "GIF" -> response.addHeader("content-type", MediaType.IMAGE_GIF_VALUE);
                case "jpg", "jpeg", "JPG" -> response.addHeader("content-type", MediaType.IMAGE_JPEG_VALUE);
                case "png", "PNG" -> response.addHeader("content-type", MediaType.IMAGE_PNG_VALUE);
                case null, default -> {
                }
            }
            inputStream.reset();
            @Cleanup OutputStream outputStream = response.getOutputStream();
            inputStream.transferTo(outputStream);
        } catch (IOException | NullPointerException e) {
            log.error("文件不存在");
            return false;
        }
        return true;
    }


    @Override
    public String deleteImage(String project, String imgName) {
        task.deleteImageInStorage(getPath(project, imgName));
        return deleteImageInfo(project, imgName);
    }

    @Override
    public List<ImageWithUrl> listImage(String project) {
        List<ImageWithUrl> imageWithUrls = new ArrayList<>();
        List<Image> images = imgMapper.listImg(project);

        images.forEach(image -> {
            ImageWithUrl imageWithUrl = new ImageWithUrl();
            BeanUtils.copyProperties(image, imageWithUrl);
            String url = String.format("http://localhost:%d/%s/%s/%s", port, webBasePath, image.getProject(), image.getName());
            imageWithUrl.setUrl(url);
            imageWithUrls.add(imageWithUrl);
        });
        return imageWithUrls;
    }

    @Override
    public List<Image> queryImageList(String id, String project, String name) {
        return imgMapper.queryImageListByIdAndProjectAndName(id, project, name);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected File createFile(String project, String fileName, String storePath) throws Exception {
        File dir = new File(storePath);
        File file = Path.of(storePath, fileName).toFile();
        if (uploadImage(file, project, fileName)) {
            throw new Exception("插入数据库失败");
        }
        if (!dir.exists()) dir.mkdirs();
        return file;
    }

    /**
     * 插入图片路径名称匹配数据到数据库
     *
     * @param file    生成的文件
     * @param project 项目名
     * @param name    文件名
     * @return 成功标志
     */
    private boolean uploadImage(File file, String project, String name) throws Exception {
        Integer integer;
        try {
            // 如果存在则先删除
            String path = imgMapper.getPath(project, name);
            if (path != null) {
                imgMapper.deleteImg(project, name);
            }
            Image image = new Image();
            image.setPath(file.getAbsolutePath());
            image.setName(name);
            image.setProject(project);
            integer = imgMapper.storeImg(image);
        } catch (Exception e) {
            throw new Exception("插入数据失败");
        }
        return integer == null;
    }

    /**
     * 保存文件已经返回文件存储信息
     */
    protected Img copyFileAndGetUrl(String project, String fileName, File file, String realpath) throws Exception {
        @Cleanup InputStream inputStream = Files.newInputStream(Paths.get(realpath));
        @Cleanup FileOutputStream outputStream = new FileOutputStream(file);
        inputStream.transferTo(outputStream);
        Img img = new Img();
        img.setProject(project);
        img.setName(fileName);
        return img;
    }


    /**
     * 获取图片路径
     *
     * @param project  项目名
     * @param fileName 图片名
     * @return 图片路径
     */
    private String getPath(String project, String fileName) {
        return imgMapper.getPath(project, fileName);
    }

    /**
     * 删除数据库中的图片信息
     *
     * @param project  项目名
     * @param fileName 文件名
     * @return 成功标志
     */
    private String deleteImageInfo(String project, String fileName) {
        Integer integer = imgMapper.deleteImg(project, fileName);
        return integer > 0 ? null : "图片数据不存在";
    }

}
