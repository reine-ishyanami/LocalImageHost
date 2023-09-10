package com.reine.imagehost.config;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author reine
 */
@Component
@Slf4j
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${server.port}")
    private int port;

    @Override
    public void onApplicationEvent(@Nonnull ApplicationStartedEvent event) {
        // 在应用程序启动后执行的代码
        log.info("Knife4j UI: http://localhost:{}/doc.html", port);
        // 添加您的自定义逻辑在这里
    }
}
