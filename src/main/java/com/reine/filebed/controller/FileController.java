package com.reine.filebed.controller;

import com.reine.filebed.entity.Result;
import com.reine.filebed.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    @Resource
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
        Map<String, String> resultMap;
        try {
            File file = transferToFile(imgFile);
            String fileName = imgFile.getOriginalFilename();
            resultMap = fileService.storeImageAPI(project, file, fileName);
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
    @GetMapping("/view/{project}/{imgName}")
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
    @DeleteMapping("/delete/{project}/{imgName}")
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
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        }
        return file;
    }

}
