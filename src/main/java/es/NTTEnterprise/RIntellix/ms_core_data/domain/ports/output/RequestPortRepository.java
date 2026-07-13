package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

/**
 * Output port for Request aggregate persistence.
 * 
 * This repository interface defines contract for persisting and retrieving
 * Request aggregates from MongoDB. As a DDD repository, it operates on
 * complete aggregates and encapsulates all data retrieval logic.
 * 
 * Since this is a READ-ONLY microservice, only retrieval operations are
 * supported.
 */
public interface RequestPortRepository {

    /**
     * Retrieves all requests from the database.
     * 
     * Used by: listRequests() use case
     * Returns: List of Request aggregates with summary information
     *
     * @return List of all Request aggregates in the database
     */
    public List<Request> findAll();

    /**
     * Retrieves a request by its unique identifier.
     * 
     * Used by: getRequestDetails() use case
     * Returns: Complete Request aggregate with all nested data
     *
     * @param requestId the unique identifier of the request
     * @return Request aggregate with the specified ID
     * @throws IllegalArgumentException if requestId is null or empty
     * @throws EntityNotFoundException  if no request is found with the given ID
     */
    public Request findById(String requestId) throws EntityNotFoundException, IllegalArgumentException;

    /**
     * Retrieves requests with dynamic filtering.
     * Only non-null parameters are applied as filters.
     *
     * @param search        the generic search term (optional filter)
     * @param partyIds      the list of matching party IDs (optional filter)
     * @param requestStatus the status of the request (optional filter)
     * @return List of requests matching the specified filters
     */
    public List<Request> findWithFilters(String search, List<String> partyIds, String requestStatus);

}