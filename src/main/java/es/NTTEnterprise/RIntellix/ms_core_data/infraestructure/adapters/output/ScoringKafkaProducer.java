package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output.strategies.ScoringTransportStrategyFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka adapter implementation of ScoringGenerationPort.
 *
 * Publishes scoring generation requests to Kafka topic "GenerateScoring"
 * for asynchronous processing by downstream scoring engine services.
 *
 * Features:
 * - Strategy-based message transformation based on request type
 * - JSON serialization of type-specific DTOs
 * - Message headers with request ID and timestamp
 * - Retry logic configured at producer level
 * - Error handling and logging
 *
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
@Slf4j
@Component
@Primary
public class ScoringKafkaProducer implements ScoringGenerationPort {

    private static final String HEADER_REQUEST_ID = "X-Request-ID";
    private static final String HEADER_TIMESTAMP = "X-Timestamp";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String generationTopic;

    public ScoringKafkaProducer(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${scoring.kafka.topic.generation}") String generationTopic) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate);
        this.generationTopic = Objects.requireNonNull(generationTopic);
    }

    /**
     * Publishes a scoring generation request to Kafka with strategy-based routing.
     *
     * Uses the strategy pattern to determine the appropriate message format
     * based on the request type from ScoringGenerationRequest. Different request
     * types send different payloads:
     * - Loans/Mortgages: Complete ScoringGenerationDTO with all features
     * - Credit Cards: CreditCardScoringGenerationDTO with focused fields
     *
     * All credit card-specific fields (creditLimit, isRevolving) are already
     * populated in ScoringGenerationRequest during the mapping phase, so the
     * strategy can work purely with ScoringGenerationRequest.
     *
     * @param scoringGenerationRequest the scoring generation domain payload with
     *                                 all features including request type
     */
    @Override
    public void publishScoringGenerationRequest(ScoringGenerationRequest scoringGenerationRequest) {
        try {
            log.debug(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_START, scoringGenerationRequest.getRequestId());

            // Select strategy based on request type from ScoringGenerationRequest
            ScoringTransportStrategy strategy = ScoringTransportStrategyFactory
                    .createStrategy(scoringGenerationRequest);

            // Build payload using the strategy
            Object payload = strategy.buildScoreGenerationPayload(scoringGenerationRequest);

            // Build message with payload and headers
            Message<?> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, generationTopic)
                    .setHeader(KafkaHeaders.KEY, scoringGenerationRequest.getRequestId())
                    .setHeader(HEADER_REQUEST_ID, scoringGenerationRequest.getRequestId())
                    .setHeader(HEADER_TIMESTAMP, System.currentTimeMillis())
                    .build();

            // Publish to Kafka
            kafkaTemplate.send(message);

            log.info(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_SUCCESS,
                    generationTopic, scoringGenerationRequest.getRequestId());
            log.debug(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_STRATEGY,
                    scoringGenerationRequest.getRequestType(),
                    scoringGenerationRequest.getRequestId());

        } catch (Exception e) {
            log.error(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_ERROR,
                    scoringGenerationRequest.getRequestId(), e.getMessage(), e);
            throw e;
        }
    }

}
