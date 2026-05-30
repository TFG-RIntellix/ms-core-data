package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
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
     * Retrieves a list of rqeuests with their detailed information based on a
     * defined number of maximun requests per page.
     * 
     * @param partyName   the name of the party that made the request, which can be
     *                    used to filter the requests by the party that made them.
     * @param partyId     the ID of the party that made the request, which can be
     *                    used to filter the requests by the party that made them.
     * @param requestType the type of request, which can be used to filter the
     *                    requests by their type (loan, mortgage or credit card).
     * @return a list of RequestSummaryDTO objects.
     */
    List<RequestSummaryDTO> listRequests(String partyName, String partyId, String requestType);

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

}
