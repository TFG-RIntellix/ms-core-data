package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

/**
 * Output port for scoring generation/publication.
 *
 * Defines the contract for publishing scoring generation events.
 * This port abstracts the mechanism for delivering scoring generation requests,
 * allowing the application layer to remain independent of specific
 * infrastructure implementations (e.g., Kafka, MQTT, REST APIs, etc.).
 *
 * Supports strategy-based message publishing where different request types
 * (loans, mortgages, credit cards) may require type-specific payloads.
 * The request type information is contained within ScoringGenerationRequest.
 *
 * @author Lucia Fernandez Mancebo
 * @Date 03-22-2026
 */
public interface ScoringGenerationPort {

    /**
     * Publishes a scoring generation request to be processed by the scoring engine.
     *
     * Uses type-specific strategies to send appropriate payloads based on request
     * type:
     * - Loans/Mortgages: ScoringGenerationDTO with complete features (21 fields)
     * - Credit Cards: CreditCardScoringGenerationDTO with focused fields (10
     * fields)
     *
     * All credit card-specific fields (creditLimit, isRevolving) are already
     * populated
     * in ScoringGenerationRequest, allowing strategy selection to be based purely
     * on
     * the request type contained within this object.
     *
     * @param scoringGenerationRequest the output payload containing scoring
     *                                 features and request type
     */
    void publishScoringGenerationRequest(ScoringGenerationRequest scoringGenerationRequest);

}