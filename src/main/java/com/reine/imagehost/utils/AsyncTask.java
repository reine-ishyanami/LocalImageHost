package com.reine.imagehost.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author reine
 */
@Component
@Slf4j
public class AsyncTask {

    @Async("asyncTaskExecutor")
    public void deleteImageInStorage(String filePath) {
        try {
            if (filePath != null) {
                Files.deleteIfExists(Path.of(filePath));
                log.info("remove image {}", filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
