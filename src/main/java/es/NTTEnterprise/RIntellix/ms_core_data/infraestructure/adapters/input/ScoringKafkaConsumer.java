package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.util.Objects;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringConsumerMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.ScoringConsumerPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka consumer adapter for receiving and processing scoring computation
 * results.
 * Listens to the PersistScoring topic in standalone mode (no consumer group).
 * Validates incoming messages, persists them to MongoDB, and sends manual
 * acknowledgment.
 * Implements error handling with retry policy.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
@Component
@Slf4j
@Validated
public class ScoringKafkaConsumer {

    private final ScoringConsumerPortService scoringConsumerService;

    /**
     * Constructs a ScoringKafkaConsumer with the required service dependency.
     * 
     * @param scoringConsumerService the application service for processing scoring
     *                               messages
     */
    public ScoringKafkaConsumer(final ScoringConsumerPortService scoringConsumerService) {
        this.scoringConsumerService = Objects.requireNonNull(scoringConsumerService);
    }

    /**
     * Consumes scoring messages from Kafka PersistScoring topic.
     * Validates message, processes through application service, and sends manual
     * acknowledgment.
     * 
     * Listens on topic: PersistScoring
     * Ack mode: MANUAL (explicit ACK after successful processing)
     * 
     * @param message        the scoring consumer message from Kafka
     * @param acknowledgment the manual acknowledgment handler
     */
    @KafkaListener(topics = "${scoring.kafka.topic.persist}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeScoring(
            @Payload @Valid ScoringConsumerMessageDTO message,
            Acknowledgment acknowledgment) {

        String requestId = message != null && message.getRequestId() != null
                ? message.getRequestId()
                : LogMessage.LOG_VALUE_UNKNOWN;

        log.info(LogMessage.KAFKA_CONSUMER_MESSAGE_RECEIVED, "PersistScoring", requestId);
        log.debug(LogMessage.KAFKA_CONSUMER_MESSAGE_PROCESSING_START, requestId,
                message != null ? message.toString() : null);

        // Process the scoring message through the application service
        Scoring scoring = scoringConsumerService.processScoringMessage(message);
        String scoringId = scoring != null ? scoring.getId() : LogMessage.LOG_VALUE_UNKNOWN;

        // Explicit ACK only on success. Error handling and retry are delegated
        // to DefaultErrorHandler.
        if (acknowledgment != null) {
            acknowledgment.acknowledge();
        }
        log.info(LogMessage.KAFKA_CONSUMER_MESSAGE_PROCESSING_SUCCESS, requestId, scoringId);
    }
}
