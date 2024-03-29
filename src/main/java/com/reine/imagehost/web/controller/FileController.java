package com.reine.imagehost.web.controller;

import com.reine.imagehost.entity.ImageWithUrl;
import com.reine.imagehost.entity.Img;
import com.reine.imagehost.entity.Result;
import com.reine.imagehost.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * 图片存储控制器
 *
 * @author reine
 * @since 2022/4/30 8:41
 */
@RestController
@RequestMapping("/${web.base.path.image}")
@Tag(name = "图片操作")
public class FileController {

    private final FileService fileService;

    public FileController(@Qualifier("fileServiceApi") FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 查询已上传的所有图片
     *
     * @param project 项目名称
     * @return
     */
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "查询已上传的所有图片")
    @Parameters({
            @Parameter(name = "project", description = "项目名称", in = ParameterIn.QUERY)
    })
    public Result<List<ImageWithUrl>> listImage(
            @RequestParam(value = "project", required = false) String project
    ) {
        List<ImageWithUrl> imgList = fileService.listImage(project);
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
    @PostMapping(value = "/{project}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "图片上传")
    @Parameters({
            @Parameter(name = "project", description = "项目名称", required = true, in = ParameterIn.PATH),
            @Parameter(name = "imgFile", description = "图片文件", required = true, in = ParameterIn.DEFAULT, ref = "imgFile"),
            @Parameter(name = "filename", description = "文件名", in = ParameterIn.QUERY)
    })
    public Result<Img> storeImage(
            @PathVariable String project,
            @RequestPart("imgFile") MultipartFile imgFile,
            @RequestParam(value = "filename", required = false) String filename
    ) {
        Img img;
        try {
            File file = transferToFile(imgFile);
            if (filename == null || filename.isEmpty()) {
                filename = imgFile.getOriginalFilename();
            }
            img = fileService.storeImage(null, project, file, filename);
        } catch (Exception e) {
            return Result.fail("上传失败");
        }
        return Result.ok("上传成功", img);
    }

    /**
     * 图片读取
     *
     * @param project 项目名称
     * @param imgName 图片名称
     * @return 成功或失败信息
     */
    @GetMapping(value = "/{project}/{imgName}")
    @Operation(summary = "图片读取")
    @Parameters({
            @Parameter(name = "project", description = "项目名称", required = true, in = ParameterIn.PATH),
            @Parameter(name = "imgName", description = "图片名称", required = true, in = ParameterIn.PATH),
    })
    public Result<Void> showImage(
            @PathVariable String project,
            @PathVariable String imgName,
            HttpServletResponse response
    ) {
        boolean flag = fileService.showImage(project, imgName, response);
        if (flag) {
            // 如果发现图片，则不返回JSON数据
            return null;
        } else {
            response.addHeader("content-type", MediaType.APPLICATION_JSON_VALUE);
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
    @DeleteMapping(value = "/{project}/{imgName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "删除图片")
    @Parameters({
            @Parameter(name = "project", description = "项目名称", required = true, in = ParameterIn.PATH),
            @Parameter(name = "imgName", description = "图片名称", required = true, in = ParameterIn.PATH),
    })
    public Result<Void> deleteImage(
            @PathVariable @Schema(description = "项目名称") String project,
            @PathVariable @Schema(description = "图片名称") String imgName
    ) {
        String result = fileService.deleteImage(project, imgName);
        if (result == null) {
            return Result.ok("图片删除成功");
        } else {
            return Result.fail(result);
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
            file = Files.createTempFile(filename[0], "." + filename[1]).toFile();
            multipartFile.transferTo(file);
            file.deleteOnExit();
        }
        return file;
    }
}
