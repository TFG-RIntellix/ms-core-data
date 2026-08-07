package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration for asynchronous method execution.
 * 
 * Enables @Async annotation processing and configures a thread pool executor
 * for handling asynchronous tasks like scoring generation.
 * 
 * Thread pool configuration:
 * - Core pool size: 2 threads (minimum concurrency)
 * - Max pool size: 5 threads (maximum concurrency)
 * - Queue capacity: 100 (maximum queued tasks before rejection)
 * - Thread name prefix: "scoring-" (for easy identification in logs)
 * 
 * @author Lucía Fernández Mancebo
 * @date 15/03/2026
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    private static final String SCORING_EXECUTOR_BEAN = "scoringExecutor";
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 5;
    private static final int QUEUE_CAPACITY = 100;
    private static final String THREAD_NAME_PREFIX = "scoring-";

    /**
     * Configures the thread pool executor used for async scoring generation tasks.
     * 
     * @return the configured ThreadPoolTaskExecutor bean for async execution
     */
    @Bean(name = SCORING_EXECUTOR_BEAN)
    public Executor scoringExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
