package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.UpdateRequestStatusDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(RequestControllerAdapter.BASE_PATH)
@Tag(name = "Requests", description = "Management of client credit and rating requests")
/**
 * Core component: RequestControllerAdapter.
 * Encapsulates the logic and responsibilities assigned to this element
 * within the Hexagonal Architecture, ensuring separation of concerns.
 *
 * @author Lucía Fernández Mancebo
 * @date 28/07/2026
 */
public class RequestControllerAdapter {

    public static final String BASE_PATH = "/api/requests";
    public static final String DETAILS_PATH = "/{requestId}";
    public static final String PARTY_PATH = "/{requestId}/party";

    private final RequestPortService requestPortService;

    public RequestControllerAdapter(RequestPortService requestPortService) {
        this.requestPortService = Objects.requireNonNull(requestPortService);
    }

    /**
     * Endpoint to retrieve a list of requests with optional filtering by party name
     * and request status.
     * Example: GET /api/requests?partyName=John%20Doe&requestStatus=pending
     * 
     * @param search        the generic search term (optional)
     * @param requestStatus the status of the request to filter by (optional)
     * @param page          the page number (0-indexed)
     * @param size          the page size
     * @param sortBy        the field to sort by
     * @param sortDir       the sort direction ("asc" or "desc")
     * @return a ResponseEntity containing a PageResponseDTO of RequestSummaryDTO objects
     *         matching the filters, or an appropriate error response if the input
     *         is invalid or an unexpected error occurs.
     */
    @GetMapping
    @Operation(summary = "List all requests", description = "Endpoint to retrieve a list of requests with optional filtering by party name and request status.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the page of requests")
    public ResponseEntity<PageResponseDTO<RequestSummaryDTO>> listRequests(
            @Parameter(description = "Generic search term for filtering requests") @RequestParam(required = false) String search,
            @Parameter(description = "The status of the request to filter by") @RequestParam(required = false) String requestStatus,
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of elements per page", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "creationDate") @RequestParam(defaultValue = "creationDate") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", BASE_PATH);
        log.debug(LogMessage.CONTROLLER_REQUEST_PARAMS, search, requestStatus);

        PageResponseDTO<RequestSummaryDTO> requests = requestPortService.listRequests(search, requestStatus, page, size, sortBy, sortDir);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, 200, requests.getContent().size());
        return ResponseEntity.ok(requests);
    }

    @GetMapping(DETAILS_PATH)
    @Operation(summary = "Get request details", description = "Retrieves the full details of a specific request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved request details"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<RequestDetailsDTO> getRequestDetails(
            @Parameter(description = "The unique identifier of the request", required = true)
            @PathVariable String requestId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", BASE_PATH + DETAILS_PATH);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);

        RequestDetailsDTO details = requestPortService.getRequestDetails(requestId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, requestId);
        return ResponseEntity.ok(details);
    }

    /**
     * Internal endpoint exposing only the party reference (id and name)
     * associated with a request. Intended for service-to-service consumers
     * (e.g. ms-reporting) so the frontend-facing details endpoint is not
     * coupled to their needs and no extra PII is exposed.
     *
     * @param requestId the unique identifier of the request
     * @return a ResponseEntity with the associated RequestPartyDTO
     */
    @GetMapping(PARTY_PATH)
    @Operation(summary = "Get request party", description = "Internal endpoint exposing only the party reference associated with a request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved request party"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<RequestPartyDTO> getRequestParty(
            @Parameter(description = "The unique identifier of the request", required = true)
            @PathVariable String requestId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", BASE_PATH + PARTY_PATH);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);

        RequestPartyDTO party = requestPortService.getRequestParty(requestId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, requestId);
        return ResponseEntity.ok(party);
    }

    /**
     * Endpoint to update the status of a request.
     * Only valid transitions are allowed (e.g., PENDIENTE_DE_REVISION → REVISADO).
     * 
     * @param requestId the unique identifier of the request
     * @param dto the DTO containing the new status value
     * @return a ResponseEntity with the updated RequestDetailsDTO
     */
    @PutMapping(DETAILS_PATH)
    @Operation(summary = "Update request status", description = "Endpoint to update the status of a request. Only valid transitions are allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated request status"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition or validation error"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    public ResponseEntity<RequestDetailsDTO> updateRequestStatus(
            @Parameter(description = "The unique identifier of the request", required = true)
            @PathVariable String requestId,
            @Parameter(description = "The new status value", required = true)
            @RequestBody @Valid UpdateRequestStatusDTO dto) {
        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "PUT", BASE_PATH + DETAILS_PATH);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);

        RequestDetailsDTO updated = requestPortService.updateRequestStatus(
                requestId, dto.getRequestStatus());

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, requestId);
        return ResponseEntity.ok(updated);
    }
}
