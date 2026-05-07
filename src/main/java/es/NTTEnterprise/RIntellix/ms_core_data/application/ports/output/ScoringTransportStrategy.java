package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output;

import org.springframework.messaging.Message;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

/**
 * Strategy interface for transforming scoring generation requests into
 * transport-specific messages for Kafka.
 *
 * Different request types (Loans, Mortgages, Credit Cards) require different
 * payloads and processing logic. This strategy pattern allows encapsulating
 * type-specific transformation and messaging concerns.
 *
 * Implementations must handle:
 * - Filtering relevant fields based on request type
 * - Creating type-specific DTOs
 * - Building Kafka messages with appropriate headers
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public interface ScoringTransportStrategy {

    /**
     * Transforms a scoring generation request into a Kafka message.
     *
     * The returned message contains the appropriate payload for the request type,
     * with headers set for routing and tracking.
     *
     * @param scoringGenerationRequest the output payload to be transported
     * @param kafkaTopic               the Kafka topic where this message will be
     *                                 published
     * @return a Message with type-specific payload and headers
     */
    Message<?> buildScoreGenerationMessage(ScoringGenerationRequest scoringGenerationRequest, String kafkaTopic);

}