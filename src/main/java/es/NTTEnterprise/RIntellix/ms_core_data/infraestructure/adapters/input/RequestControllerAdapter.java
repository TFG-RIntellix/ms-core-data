package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestPartyDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(RequestControllerAdapter.BASE_PATH)
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
     * @param partyName   the name of the party to filter requests by (optional)
     * @param requestType the type of request to filter by (optional)
     * @return a ResponseEntity containing a list of RequestSummaryDTO objects
     *         matching the filters, or an appropriate error response if the input
     *         is invalid or an unexpected error occurs.
     * 
     */
    @GetMapping
    public ResponseEntity<List<RequestSummaryDTO>> listRequests(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String requestStatus) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", BASE_PATH);
        log.debug(LogMessage.CONTROLLER_REQUEST_PARAMS, search, requestStatus);

        List<RequestSummaryDTO> requests = requestPortService.listRequests(search, requestStatus);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, 200, requests.size());
        return ResponseEntity.ok(requests);
    }

    @GetMapping(DETAILS_PATH)
    public ResponseEntity<RequestDetailsDTO> getRequestDetails(@PathVariable String requestId) {

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
    public ResponseEntity<RequestPartyDTO> getRequestParty(@PathVariable String requestId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", BASE_PATH + PARTY_PATH);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);

        RequestPartyDTO party = requestPortService.getRequestParty(requestId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, requestId);
        return ResponseEntity.ok(party);
    }
}
