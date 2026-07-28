package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka consumer configuration for scoring persistence messages.
 * 
 * Configures the consumer factory and listener container factory for receiving
 * messages from the PersistScoring topic in standalone mode (no consumer
 * group).
 * 
 * Consumer settings:
 * - Bootstrap servers: configured in application.properties
 * - Key deserializer: StringDeserializer
 * - Value deserializer: JsonDeserializer (for ScoringConsumerMessageDTO)
 * - Acknowledgment mode: MANUAL (explicitly acknowledge after successful
 * processing)
 * - Auto-offset-reset: earliest (start from beginning if offset not found)
 * - Auto-commit: disabled (no automatic commits with manual ACK)
 * - Max poll records: 100 (batch size)
 * - Session timeout: 30s
 * - Retry logic: fixed backoff with configurable attempts
 * - Error handling: selective retries based on exception type
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
@Configuration
@Slf4j
@EnableKafka
public class KafkaConsumerConfig {

    private static final String AUTO_OFFSET_RESET_EARLIEST = "earliest";
    private static final String TRUSTED_PACKAGES_ALL = "*";
    private static final int MAX_POLL_RECORDS = 100;
    private static final int SESSION_TIMEOUT_MS = 30000;
    private static final int SINGLE_CONSUMER_CONCURRENCY = 1;

    private final String bootstrapServers;
    private final String groupId;
    private final int maxRetryAttempts;
    private final long initialDelayMs;

    public KafkaConsumerConfig(
            @Value("${spring.kafka.bootstrap-servers}") final String bootstrapServers,
            @Value("${scoring.kafka.consumer.group-id}") final String groupId,
            @Value("${scoring.kafka.consumer.retry.max-attempts}") final int maxRetryAttempts,
            @Value("${scoring.kafka.consumer.retry.initial-delay}") final long initialDelayMs) {
        this.bootstrapServers = Objects.requireNonNull(bootstrapServers);
        this.groupId = Objects.requireNonNull(groupId);
        this.maxRetryAttempts = maxRetryAttempts;
        this.initialDelayMs = initialDelayMs;
    }

    /**
     * Configures the ConsumerFactory for Kafka message listeners.
     * Deserializes JSON messages into ScoringConsumerMessageDTO objects.
     * 
     * @return ConsumerFactory configured with manual acknowledgment settings
     */
    @Bean
    public ConsumerFactory<String, ScoringResultMessageDTO> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ScoringResultMessageDTO.class.getName());
        configProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, TRUSTED_PACKAGES_ALL);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET_EARLIEST);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, SESSION_TIMEOUT_MS);

        // Error handling deserializer wrapper
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());

        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    /**
     * Configures the listener container factory for Kafka message listeners.
     * Sets up manual acknowledgment mode and error handling.
     * 
     * @param consumerFactory the consumer factory bean
     * @return ConcurrentKafkaListenerContainerFactory configured for the consumer
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ScoringResultMessageDTO> kafkaListenerContainerFactory(
            ConsumerFactory<String, ScoringResultMessageDTO> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, ScoringResultMessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setCommonErrorHandler(kafkaErrorHandler());
        factory.setConcurrency(SINGLE_CONSUMER_CONCURRENCY);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    /**
     * Configures method-level payload validation for Kafka listeners using
     * Bean Validation annotations (for example, @Valid on @Payload parameters).
     * 
     * @param validator bean validation factory
     * @return Kafka listener configurer with validation-enabled method factory
     */
    @Bean
    public KafkaListenerConfigurer kafkaListenerConfigurer(final LocalValidatorFactoryBean validator) {
        return registrar -> {
            final DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
            factory.setValidator(validator);
            factory.afterPropertiesSet();
            registrar.setMessageHandlerMethodFactory(factory);
        };
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(
            final LocalValidatorFactoryBean validator) {

        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setValidator(validator);
        return processor;
    }

    /**
     * Configures the error handler for failed messages.
     * Implements selective retry logic based on exception type.
     * 
     * Non-retryable exceptions are skipped immediately.
     * Retryable exceptions are retried according to configured backoff.
     * 
     * @return CommonErrorHandler configured with retry logic
     */
    @Bean
    public CommonErrorHandler kafkaErrorHandler() {

        FixedBackOff backoff = new FixedBackOff(initialDelayMs, maxRetryAttempts);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {

            // Identify Bean Validation failures to log structured validation details.
            if (exception instanceof ListenerExecutionFailedException &&
                    exception.getCause() instanceof MethodArgumentNotValidException) {

                MethodArgumentNotValidException validationEx = (MethodArgumentNotValidException) exception.getCause();

                log.warn(LogMessage.KAFKA_CONSUMER_VALIDATION_REJECTED,
                        consumerRecord.offset(),
                        validationEx.getBindingResult().getErrorCount());

                validationEx.getBindingResult().getAllErrors().forEach(error -> {
                    String fieldName = error.getObjectName() + ".";
                    String message = error.getDefaultMessage();

                    log.warn(LogMessage.KAFKA_CONSUMER_VALIDATION_DETAIL,
                            fieldName,
                            message);
                });
            } else {
                // For non-validation exceptions, log full stack trace for troubleshooting.
                log.error(LogMessage.KAFKA_CONSUMER_ERROR,
                        consumerRecord.offset(),
                        exception.getMessage(), exception);
            }
        }, backoff);

        errorHandler.addNotRetryableExceptions(MethodArgumentNotValidException.class);
        return errorHandler;
    }
}
