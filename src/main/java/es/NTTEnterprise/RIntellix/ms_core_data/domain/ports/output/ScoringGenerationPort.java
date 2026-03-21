package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;

/**
 * Output port for scoring generation/publication.
 * 
 * Defines the contract for publishing scoring generation events.
 * This port abstracts the mechanism for delivering scoring generation requests,
 * allowing the domain layer to remain independent of specific infrastructure
 * implementations (e.g., Kafka, MQTT, REST APIs, etc.).
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-15-2026
 */
public interface ScoringGenerationPort {

    /**
     * Publishes a scoring generation request to be processed by the scoring engine.
     * 
     * @param scoringGenerationDTO the DTO containing all features for scoring
     *                             generation
     * @throws Exception if the publication fails
     */
    void publishScoringGenerationRequest(ScoringGenerationDTO scoringGenerationDTO) throws Exception;
}
