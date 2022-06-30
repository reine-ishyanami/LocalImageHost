package com.reine.filebed.controller;

import com.reine.filebed.entity.Result;
import com.reine.filebed.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 图片存储控制器
 *
 * @author reine
 * @since 2022/4/30 8:41
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 图片上传
     *
     * @param project 项目名称
     * @param imgFile 图片
     * @return 成功或失败信息
     */
    @PostMapping("/upload/{project}")
    public Result storeImage(@PathVariable String project, @RequestParam("imgFile") MultipartFile imgFile) {
        File file = transferToFile(imgFile);
        String fileName = imgFile.getOriginalFilename();
        String s = fileService.storeImage(project, file, fileName);
        return new Result(2001, "上传成功", s);
    }

    /**
     * 图片读取
     *
     * @param project 项目名称
     * @param imgName 图片名称
     * @return 成功或失败信息
     */
    @GetMapping("/view/{project}/{imgName}")
    public Result showImage(@PathVariable String project, @PathVariable String imgName, HttpServletResponse response) {
        boolean flag = fileService.showImage(project, imgName, response);
        if (flag) {
            return new Result(2002, "图片展示成功", null);
        } else {
            return new Result(5001, "图片展示失败", null);
        }
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
        boolean flag = fileService.deleteImage(project, imgName);
        if (flag) {
            return new Result(2003, "图片删除成功", null);
        } else {
            return new Result(5002, "图片删除失败", null);
        }
    }

    /**
     * multipartFile转file
     *
     * @param multipartFile 文件
     * @return 文件
     */
    private File transferToFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        File file = null;
        if (originalFilename != null) {
            file = new File(originalFilename);
            try {

                String[] filename = originalFilename.split("\\.");
                file = File.createTempFile(filename[0], filename[1]);
                multipartFile.transferTo(file);
                file.deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

}
