package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka adapter implementation of ScoringGenerationPort.
 * 
 * Publishes scoring generation requests to Kafka topic "GenerateScoring"
 * for asynchronous processing by downstream scoring engine services.
 * 
 * Features:
 * - JSON serialization of ScoringGenerationDTO
 * - Message headers with request ID and timestamp
 * - Retry logic configured at producer level
 * - Error handling and logging
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-15-2026
 */
@Slf4j
@Component
@org.springframework.context.annotation.Primary
public class ScoringKafkaAdapter implements ScoringGenerationPort {

    private final KafkaTemplate<String, ScoringGenerationDTO> kafkaTemplate;
    private final String generationTopic;

    public ScoringKafkaAdapter(
            KafkaTemplate<String, ScoringGenerationDTO> kafkaTemplate,
            @Value("${scoring.kafka.topic.generation}") String generationTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.generationTopic = generationTopic;
    }

    /**
     * Publishes a scoring generation request to Kafka.
     * 
     * The message is sent to the configured topic with the request ID as key
     * and the DTO as value. Additional headers include timestamp for tracking
     * and request ID for correlation.
     * 
     * @param scoringGenerationDTO the scoring generation request DTO
     * @throws Exception if message publishing fails
     */
    @Override
    public void publishScoringGenerationRequest(ScoringGenerationDTO scoringGenerationDTO) throws Exception {
        try {
            log.debug(LogMessage.SERVICE_GET_DETAILS_START, scoringGenerationDTO.getRequestId());

            // Build message with key (requestId) and value (DTO)
            Message<ScoringGenerationDTO> message = MessageBuilder
                    .withPayload(scoringGenerationDTO)
                    .setHeader(KafkaHeaders.TOPIC, generationTopic)
                    .setHeader(KafkaHeaders.KEY, scoringGenerationDTO.getRequestId())
                    .setHeader("X-Request-ID", scoringGenerationDTO.getRequestId())
                    .setHeader("X-Timestamp", System.currentTimeMillis())
                    .build();

            // Publish to Kafka
            kafkaTemplate.send(message);

            log.info("Scoring generation request published to Kafka topic '{}' for request ID: {}",
                    generationTopic, scoringGenerationDTO.getRequestId());
            log.debug("Published DTO: {}", scoringGenerationDTO);

        } catch (Exception e) {
            log.error("Error publishing scoring generation request to Kafka for request ID: {}",
                    scoringGenerationDTO.getRequestId(), e);
            throw e;
        }
    }
}
