package com.reine.filebed.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author reine
 * 2022/6/30 9:24
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value("${local.store}")
    private String localStore;

    @Override
    public String storeImage(String project, File imgFile) {
        String filename = imgFile.getName();
        String storePath = localStore + project;
        File dir = new File(storePath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        // 拼接文件路径
        String filePath = storePath + "//" + filename;
        // 数据缓冲区
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        File file = new File(filePath);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        int i = imgFile.getPath().indexOf(":");
        String Realpath = imgFile.getPath().substring(i + 2);
        log.info("Realpath---{}",Realpath);
        try {
            inputStream = Files.newInputStream(Paths.get(Realpath));
            outputStream = new FileOutputStream(file);
            while ((len = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(inputStream, outputStream);
        }
        return "/" + project + "/" + filename;
    }

    @Override
    public String storeImage(String project, File imgFile, String fileName) {
        String storePath = localStore + project;
        File dir = new File(storePath);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        // 拼接文件路径
        String filePath = storePath + "//" + fileName;
        // 数据缓冲区
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        File file = new File(filePath);
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = Files.newInputStream(imgFile.toPath());
            outputStream = new FileOutputStream(file);
            while ((len = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeStream(inputStream, outputStream);
        }
        return "/" + project + "/" + fileName;
    }

    @Override
    public boolean showImage(String project, String imgName, HttpServletResponse response) {
        String filePath = localStore + project + "//" + imgName;
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            int i = inputStream.available();
            //byte数组用于存放图片字节数据
            byte[] buffer = new byte[i];
            int read = inputStream.read(buffer);
            //设置发送到客户端的响应内容类型
            response.setContentType("image/*");
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
        String filePath = localStore + project + "//" + imgName;
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            boolean delete = file.delete();
            return true;
        }
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
}
