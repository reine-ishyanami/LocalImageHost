package com.reine.imagehost.web.controller;

import com.reine.imagehost.entity.Result;
import com.reine.imagehost.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 图片存储控制器
 *
 * @author reine
 * @since 2022/4/30 8:41
 */
@RestController
public class FileController {

    private FileService fileService;

    @GetMapping("/list")
    public Result listImage(@RequestParam(value = "project", required = false) String project) {
        Map<String, Object> imgList = fileService.listImage(project);
        return Result.ok("查询成功", imgList);
    }

    /**
     * 图片上传
     *
     * @param project  项目名称
     * @param imgFile  图片文件
     * @param filename 文件名
     * @return 成功或失败信息
     */
    @PostMapping("/{project}")
    public Result storeImage(@PathVariable String project,
                             @RequestParam("imgFile") MultipartFile imgFile,
                             @RequestParam(value = "filename", required = false) String filename) {
        Map<String, Object> resultMap;
        try {
            File file = transferToFile(imgFile);
            if (filename == null || "".equals(filename)) {
                filename = imgFile.getOriginalFilename();
            }
            resultMap = fileService.storeImageAPI(project, file, filename);
        } catch (Exception e) {
            return Result.fail("上传失败");
        }
        return Result.ok("上传成功", resultMap);
    }

    /**
     * 图片读取
     *
     * @param project 项目名称
     * @param imgName 图片名称
     * @return 成功或失败信息
     */
    @GetMapping("/{project}/{imgName}")
    public Result showImage(@PathVariable String project, @PathVariable String imgName, HttpServletResponse response) {
        boolean flag = fileService.showImage(project, imgName, response);
        if (flag) {
            return Result.ok("图片展示成功");
        } else {
            return Result.fail("图片展示失败");
        }
    }

    /**
     * 删除图片
     *
     * @param project 项目名称
     * @param imgName 图片名称
     * @return 成功或失败信息
     */
    @DeleteMapping("/{project}/{imgName}")
    public Result deleteImage(@PathVariable String project, @PathVariable String imgName) {
        boolean flag = fileService.deleteImage(project, imgName);
        if (flag) {
            return Result.ok("图片删除成功");
        } else {
            return Result.fail("图片删除失败");
        }
    }

    /**
     * multipartFile转file
     *
     * @param multipartFile 文件
     * @return 文件
     */
    private File transferToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        File file = null;
        if (originalFilename != null) {
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], "." + filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        }
        return file;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
