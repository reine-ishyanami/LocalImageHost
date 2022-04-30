package com.reine.filebed.controller;

import com.reine.filebed.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 图片存储控制器
 * @author reine
 * @since 2022/4/30 8:41
 */
@RestController
public class FileController {

    @Value("${local.store}")
    private String localStore;

    /**
     * 图片上传
     *
     * @param project 项目名称
     * @param imgFile 图片
     * @return 成功或失败信息
     */
    @PostMapping("/upload/{project}")
    public Result storeImage(@PathVariable String project, @RequestParam("imgFile") MultipartFile imgFile) {
        // 重新生成文件名
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String extensionName = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID() + extensionName;

        // 判断文件夹是否存在，不存在则创建文件夹
        String storePath = localStore + project;
        File dir = new File(storePath);
        if (!dir.exists()) {
            dir.mkdirs();
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
            inputStream = imgFile.getInputStream();
            outputStream = new FileOutputStream(file);
            while ((len = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(500, "上传失败", null);
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Result(200, "上传成功", project + "/" + fileName);
    }

    /**
     * 图片读取
     *
     * @param project 项目名称
     * @param imgName 图片名称
     * @return 成功或失败信息
     */
    @GetMapping("/download/{project}/{imgName}")
    public Result showImage(@PathVariable String project, @PathVariable String imgName, HttpServletResponse response) {
        String filePath = localStore + "//" + project + "//" + imgName;
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            int i = inputStream.available();
            //byte数组用于存放图片字节数据
            byte[] buffer = new byte[i];
            inputStream.read(buffer);
            //设置发送到客户端的响应内容类型
            response.setContentType("image/*");
            outputStream = response.getOutputStream();
            outputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(500,"图片展示失败",null);

        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Result(200,"图片展示成功",null);
    }

    /**
     * 删除图片
     *
     * @param project 项目名称
     * @param imgName 图片名称
     * @return 成功或失败信息
     */
    @DeleteMapping("/delete/{project}/{imgName}")
    public Result deleteImage(@PathVariable String project, @PathVariable String imgName) {
        String filePath = localStore + "//" + project + "//" + imgName;
        File file = new File(filePath);
        if (!file.exists()) {
            return new Result(500, "图片不存在", null);
        } else {
            file.delete();
            return new Result(500, "图片删除成功", null);
        }
    }

}
