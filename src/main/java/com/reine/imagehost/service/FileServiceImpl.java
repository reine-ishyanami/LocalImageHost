package com.reine.imagehost.service;

import com.reine.imagehost.entity.Image;
import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.entity.Img;
import com.reine.imagehost.mapper.ImgMapper;
import com.reine.imagehost.utils.AsyncTask;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author reine
 * 2022/6/30 9:24
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${local.store}")
    private String localStore;

    @Value("${server.port}")
    private Integer port;

    @Value("${web.base.path.image}")
    private String webBasePath;

    private final ImgMapper imgMapper;

    @Override
    public Img storeImageGUI(String path, String project, File imgFile) throws Exception {
        String fileName = imgFile.getName();
        String storePath = path + File.separator + project;
        // 拼接文件路径
        File file = createFile(project, fileName, storePath);
        int i = imgFile.getPath().indexOf(":");
        String realpath = imgFile.getPath().substring(i + 2);
        return copyFileAndGetUrl(project, fileName, file, realpath);
    }

    @Override
    public Img storeImageAPI(String project, File imgFile, String fileName) throws Exception {
        String storePath = localStore + File.separator + project;
        File file = createFile(project, fileName, storePath);
        String realpath = imgFile.getAbsolutePath();
        return copyFileAndGetUrl(project, fileName, file, realpath);
    }

    private Img copyFileAndGetUrl(String project, String fileName, File file, String realpath) throws Exception {
        // 数据缓冲区
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = Files.newInputStream(Paths.get(realpath));
            outputStream = new FileOutputStream(file);
            while ((len = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("写入文件失败");
        } finally {
            closeStream(inputStream, outputStream);
        }
        Img img = new Img();
        img.setProject(project);
        img.setName(fileName);
        return img;
    }

    private File createFile(String project, String fileName, String storePath) throws Exception {
        File dir = new File(storePath);
        File file = Path.of(storePath, fileName).toFile();
        if (uploadImage(file, project, fileName)) {
            throw new Exception("插入数据库失败");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return file;
    }

    @Override
    public boolean showImage(String project, String imgName, HttpServletResponse response) {
        String filePath = getPath(project, imgName);
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            int i = inputStream.available();
            // byte数组用于存放图片字节数据
            byte[] buffer = new byte[i];
            inputStream.read(buffer);
            outputStream = response.getOutputStream();
            outputStream.write(buffer);
        } catch (IOException | NullPointerException e) {
            log.error("文件不存在");
            return false;
        } finally {
            closeStream(inputStream, outputStream);
        }
        return true;
    }

    private final AsyncTask task;

    @Override
    public String deleteImage(String project, String imgName) {
        task.deleteImageInStorage(getPath(project, imgName));
        return deleteImageInfo(project, imgName);
    }

    @Override
    public void createTable() {
        imgMapper.createTable();
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

    /**
     * 关闭流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     */
    private void closeStream(InputStream inputStream, OutputStream outputStream) {
        Optional.ofNullable(outputStream).ifPresent(fileOutputStream -> {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Optional.ofNullable(inputStream).ifPresent(fileInputStream -> {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
