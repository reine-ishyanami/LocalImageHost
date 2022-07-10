package com.reine.filebed.service;

import com.reine.filebed.entity.Image;
import com.reine.filebed.mapper.ImgMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author reine
 * 2022/6/30 9:24
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements FileService {

    @Value("${local.store}")
    private String localStore;

    @Resource
    private ImgMapper imgMapper;

    @Override
    public Map<String, String> storeImageGUI(String path, String project, File imgFile) throws Exception {
        String fileName = imgFile.getName();
        String storePath = path + "\\" + project;
        // 拼接文件路径
        File file = createFile(project, fileName, storePath);
        int i = imgFile.getPath().indexOf(":");
        String Realpath = imgFile.getPath().substring(i + 2);
        return copyFileAndGetUrl(project, fileName, file, Realpath);
    }

    @Override
    public Map<String, String> storeImageAPI(String project, File imgFile, String fileName) throws Exception {
        String storePath = localStore + project;
        File file = createFile(project, fileName, storePath);
        String Realpath = imgFile.getAbsolutePath();
        return copyFileAndGetUrl(project, fileName, file, Realpath);
    }

    private Map<String, String> copyFileAndGetUrl(String project, String fileName, File file, String Realpath) throws Exception {
        // 数据缓冲区
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = Files.newInputStream(Paths.get(Realpath));
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
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("project", project);
        resultMap.put("fileName", fileName);
        return resultMap;
    }

    private File createFile(String project, String fileName, String storePath) throws Exception {
        File dir = new File(storePath);
        String filePath = storePath + "\\" + fileName;
        File file = new File(filePath);
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
            //byte数组用于存放图片字节数据
            byte[] buffer = new byte[i];
            inputStream.read(buffer);
            outputStream = response.getOutputStream();
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(inputStream, outputStream);
        }
        return true;
    }

    @Override
    public boolean deleteImage(String project, String imgName) {
        String filePath = getPath(project, imgName);
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            file.delete();
            return deleteImageInfo(project, imgName);
        }
    }

    @Override
    public void createTable() {
        imgMapper.createTable();
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
    private boolean deleteImageInfo(String project, String fileName) {
        Integer integer = imgMapper.deleteImg(project, fileName);
        return integer != null;
    }
}
