package com.asi.hms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private static final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    @Value("${deployment.executor.core-pool-size}")
    private int corePoolSize;

    @Bean(name = "deploymentExecutor")
    public Executor deploymentExecutor() {

        logger.info("Creating deploymentExecutor with corePoolSize: {}", corePoolSize);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize); // Number of concurrent threads
        executor.setMaxPoolSize(corePoolSize);  // Maximum number of concurrent threads
        executor.setQueueCapacity(500); // Queue capacity to hold tasks before they're executed
        executor.setThreadNamePrefix("DeploymentExecutor-");
        executor.initialize();

        return executor;
    }

}
