package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.ScoringGenerationTransportDTOMapper;
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
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
@Slf4j
@Component
@Primary
public class ScoringKafkaProducer implements ScoringGenerationPort {

    private static final String HEADER_REQUEST_ID = "X-Request-ID";
    private static final String HEADER_TIMESTAMP = "X-Timestamp";

    private final KafkaTemplate<String, ScoringGenerationDTO> kafkaTemplate;
    private final String generationTopic;

    public ScoringKafkaProducer(
            KafkaTemplate<String, ScoringGenerationDTO> kafkaTemplate,
            @Value("${scoring.kafka.topic.generation}") String generationTopic) {
        this.kafkaTemplate = Objects.requireNonNull(kafkaTemplate);
        this.generationTopic = Objects.requireNonNull(generationTopic);
    }

    /**
     * Publishes a scoring generation request to Kafka.
     *
     * The message is sent to the configured topic with the request ID as key
     * and the DTO as value. Additional headers include timestamp for tracking
     * and request ID for correlation.
     *
     * @param scoringGenerationRequest the scoring generation domain payload
     */
    @Override
    public void publishScoringGenerationRequest(ScoringGenerationRequest scoringGenerationRequest) {
        ScoringGenerationDTO scoringGenerationDTO = ScoringGenerationTransportDTOMapper
                .toTransportDTO(scoringGenerationRequest);
        try {
            log.debug(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_START, scoringGenerationDTO.getRequestId());

            // Build message with key (requestId) and value (DTO)
            Message<ScoringGenerationDTO> message = MessageBuilder
                    .withPayload(scoringGenerationDTO)
                    .setHeader(KafkaHeaders.TOPIC, generationTopic)
                    .setHeader(KafkaHeaders.KEY, scoringGenerationDTO.getRequestId())
                    .setHeader(HEADER_REQUEST_ID, scoringGenerationDTO.getRequestId())
                    .setHeader(HEADER_TIMESTAMP, System.currentTimeMillis())
                    .build();

            // Publish to Kafka
            kafkaTemplate.send(message);

            log.info(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_SUCCESS,
                    generationTopic, scoringGenerationDTO.getRequestId());
            log.debug(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_DTO, scoringGenerationDTO);

        } catch (Exception e) {
            log.error(LogMessage.KAFKA_SCORING_GENERATION_PUBLISH_ERROR,
                    scoringGenerationDTO.getRequestId(), e.getMessage(), e);
            throw e;
        }
    }
}
