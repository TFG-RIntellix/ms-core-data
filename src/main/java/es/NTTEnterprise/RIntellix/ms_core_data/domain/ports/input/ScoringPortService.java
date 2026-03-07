package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

/**
 * Input port for scoring operations.
 * Defines the use cases available for scoring retrieval.
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface ScoringPortService {

    /**
     * Retrieves the current (most recent) scoring associated with a given request.
     * @param requestId The ID of the request whose scoring is to be retrieved.
     * @return The scoring DTO with all scoring details.
     * @throws IllegalArgumentException  If the requestId is null or blank.
     * @throws EntityNotFoundException   If no scoring is found for the given request.
     */
    ScoringDTO getScoringByRequestId(String requestId) throws IllegalArgumentException, EntityNotFoundException;

}
