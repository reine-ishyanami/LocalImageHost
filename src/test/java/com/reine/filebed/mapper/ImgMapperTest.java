package com.reine.filebed.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author reine
 * 2022/7/6 21:58
 */
@SpringBootTest
@Slf4j
public class ImgMapperTest {

    @Autowired
    private ImgMapper imgMapper;

    @Test
    void selectPath() {
        String path = imgMapper.getPath("aaa","111");
        log.info("path---{}", path);
    }

    @Test
    void deleteImgInfo() {
        Integer integer = imgMapper.deleteImg("aaa","222");
        if (integer != null) {
            log.info("delete success");
        }
    }
}
