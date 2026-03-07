package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

/**
 * Output port for scoring persistence operations.
 * Defines the repository contract for scoring data access.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface ScoringPortRepository {

    /**
     * Finds the most recent scoring for a given request ID.
     * 
     * @param requestId The ID of the request.
     * @return The associated scoring for the given request.
     * @throws EntityNotFoundException If no scoring is found for the given request.
     */
    Scoring findByRequestId(String requestId) throws EntityNotFoundException;

    /**
     * Finds a scoring by its unique identifier.
     * 
     * @param scoringId The unique identifier of the scoring.
     * @return The scoring with the given ID.
     * @throws EntityNotFoundException If no scoring is found with the given ID.
     */
    Scoring findById(String scoringId) throws EntityNotFoundException;

}
