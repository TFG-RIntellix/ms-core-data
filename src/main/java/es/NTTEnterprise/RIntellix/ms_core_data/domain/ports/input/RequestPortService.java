package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

/**
 * Interface that defines the contract for request service operations.
 * 
 * This service interface provides the abstraction layer for handling
 * request-related business logic and operations within the application.
 * Implementations of this interface should handle the processing,
 * validation, and coordination of incoming requests.
 *
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public interface RequestPortService {

    /**
     * Retrieves all requests with optional filtering.
     * 
     * @param search        a generic search term to match against request ID or party name (optional filter)
     * @param requestStatus the status of the request (optional filter)
     * @return List of request summary DTOs
     */
    List<RequestSummaryDTO> listRequests(String search, String requestStatus);

    /**
     * Retrieves the detailed information of a specific request.
     * 
     * @param requestId the unique identifier of the request.
     * @return a RequestDetailsDTO object with the detailed information of the
     *         request.
     * @throws IllegalArgumentException if the requestId is null or empty.
     * @throws EntityNotFoundException  if no request is found with the given
     *                                  requestId.
     */
    RequestDetailsDTO getRequestDetails(String requestId) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Retrieves the party identifiers (id and name) associated with a request.
     *
     * Intended for internal service-to-service consumers that only need the
     * party reference, avoiding exposure of the full request/party detail.
     *
     * @param requestId the unique identifier of the request.
     * @return a RequestPartyDTO with the associated party id and name.
     * @throws IllegalArgumentException if the requestId is null or empty.
     * @throws EntityNotFoundException  if no request is found with the given
     *                                  requestId.
     */
    RequestPartyDTO getRequestParty(String requestId) throws IllegalArgumentException, EntityNotFoundException;

}
