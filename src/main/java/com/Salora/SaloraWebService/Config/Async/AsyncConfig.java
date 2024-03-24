package com.Salora.SaloraWebService.Config.Async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "AsyncTaskExecutor")
    public Executor asyncTaskExecute(){
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(4);
        threadPoolExecutor.setQueueCapacity(100);
        threadPoolExecutor.setMaxPoolSize(4);
        threadPoolExecutor.setThreadNamePrefix("AsyncTaskThread-");
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
