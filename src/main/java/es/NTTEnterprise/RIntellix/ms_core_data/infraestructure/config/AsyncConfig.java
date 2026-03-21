package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.config;

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
 * @Date 03-15-2026
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Configures the thread pool executor used for async scoring generation tasks.
     * 
     * @return the configured ThreadPoolTaskExecutor bean for async execution
     */
    @Bean(name = "scoringExecutor")
    public Executor scoringExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("scoring-");
        executor.initialize();
        return executor;
    }
}
