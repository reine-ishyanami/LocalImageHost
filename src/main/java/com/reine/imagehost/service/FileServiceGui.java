package com.reine.imagehost.service;

import com.reine.imagehost.entity.Img;
import com.reine.imagehost.mapper.ImgMapper;
import com.reine.imagehost.utils.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * JavaFX GUI调用时使用的服务
 *
 * @author reine
 * 2024/3/6 11:54
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceGui extends AbstractFileService implements FileService {

    public FileServiceGui(ImgMapper imgMapper, AsyncTask task) {
        super(imgMapper, task);
    }

    @Override
    public Img storeImage(String path, String project, File imgFile, String fileName) throws Exception {
        String name = StringUtils.isBlank(fileName) ? imgFile.getName() : fileName;
        String storePath = path + File.separator + project;
        // 拼接文件路径
        File file = createFile(project, name, storePath);
        int i = imgFile.getPath().indexOf(":");
        String realpath = imgFile.getPath().substring(i + 2);
        return copyFileAndGetUrl(project, name, file, realpath);
    }


}
