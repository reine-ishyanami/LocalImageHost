package com.reine.imagehost.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author reine
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(10);
        // 最大线程数
        executor.setMaxPoolSize(50);
        // 缓存队列
        executor.setQueueCapacity(20);
        // 允许的空闲时间
        executor.setKeepAliveSeconds(180);
        // 线程名称前缀
        executor.setThreadNamePrefix("async-task-");
        // 拒绝策略（重试）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
