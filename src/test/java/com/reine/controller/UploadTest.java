package com.reine.controller;

import com.reine.filebed.entity.Result;
import com.reine.filebed.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author reine
 * @since 2022/4/30 15:10
 */
public class UploadTest {

    /**
     * 测试上传png图片
     */
    @Test
    public void testUploadUtils() throws IOException {
        File file = new File("../../../../../../../../Users/86158/图片/110300202.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
        Result test = FileUtils.upload("test", multipartFile);
        System.out.println(test.getProjectAndFileName());
    }

    @Test
    public void testDeleteUtils(){
        Result test = FileUtils.delete("");
        System.out.println(test.getMessage());
    }

}
