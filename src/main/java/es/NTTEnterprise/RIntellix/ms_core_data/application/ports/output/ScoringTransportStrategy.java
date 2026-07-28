package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output;



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
 * @author Lucía Fernández Mancebo
 * @date 05/04/2026
 */
public interface ScoringTransportStrategy {

    Object buildScoreGenerationPayload(ScoringGenerationRequest scoringGenerationRequest);

}
