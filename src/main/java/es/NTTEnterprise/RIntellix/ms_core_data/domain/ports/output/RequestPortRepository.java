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
     * @throws EntityNotFoundException if no request is found with the given ID
     */
    public Request findById(String requestId) throws EntityNotFoundException, IllegalArgumentException;

    /**
     * Retrieves all requests made by a specific party.
     *
     * @param partyName the name of the party
     * @return List of requests made by the specified party
     */
    public List<Request> findByPartyName(String partyName);

    /**
     * Retrieves all requests of a specific status.
     *
     * @param requestStatus the status of the request.
     * @return List of requests with the specified status
     */
    public List<Request> findByStatus(String status);

}