package com.reine.imagehost.service;

import com.reine.imagehost.entity.Img;
import com.reine.imagehost.mapper.ImgMapper;
import com.reine.imagehost.utils.AsyncTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * 网页API调用时使用的服务
 *
 * @author reine
 * 2024/3/6 11:54
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceApi extends AbstractFileService implements FileService {

    @Value("${local.store}")
    private String localStore;

    public FileServiceApi(ImgMapper imgMapper, AsyncTask task) {
        super(imgMapper, task);
    }


    @Override
    public Img storeImage(String path, String project, File imgFile, String fileName) throws Exception {
        String storePath = localStore + File.separator + project;
        File file = createFile(project, fileName, storePath);
        String realpath = imgFile.getAbsolutePath();
        return copyFileAndGetUrl(project, fileName, file, realpath);
    }


}
