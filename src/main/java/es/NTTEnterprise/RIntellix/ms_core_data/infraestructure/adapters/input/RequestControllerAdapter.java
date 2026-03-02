package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/api/requests")
public class RequestControllerAdapter {
    
    private final RequestPortService requestPortService;

    public RequestControllerAdapter(RequestPortService requestPortService) {
        this.requestPortService = requestPortService;
    }

    /**
     * Endpoint to retrieve a list of requests with optional filtering by party name and request status.
     * Example: GET /api/requests?partyName=John%20Doe&requestStatus=pending
     * @param partyName the name of the party to filter requests by (optional)
     * @param requestType the type of request to filter by (optional)
     * @return a ResponseEntity containing a list of RequestSummaryDTO objects matching the filters, or an appropriate error response if the input is invalid or an unexpected error occurs.
     * 
     */
    @GetMapping
    public ResponseEntity<List<RequestSummaryDTO>> listRequests(
            @RequestParam(required = false) String partyName,
            @RequestParam(required = false) String requestStatus) {
        
        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/requests");
        log.debug(LogMessage.CONTROLLER_REQUEST_PARAMS, partyName, requestStatus);
        
        try {
            List<RequestSummaryDTO> requests = requestPortService.listRequests(partyName, requestStatus);
            
            log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, HttpStatus.OK.value(), requests.size());
            return ResponseEntity.ok(requests);
            
        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, e.getMessage());
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().build();
            
        } catch (Exception e) {
            log.error(LogMessage.CONTROLLER_UNEXPECTED_ERROR, e.getMessage(), e);
            log.error(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDetailsDTO> getRequestDetails(@PathVariable String requestId) {
        
        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/requests/" + requestId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);
        
        try {
            RequestDetailsDTO details = requestPortService.getRequestDetails(requestId);
            
            log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, HttpStatus.OK.value(), requestId);
            return ResponseEntity.ok(details);
            
        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, e.getMessage());
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().build();
            
        } catch (EntityNotFoundException e) {
            log.warn(LogMessage.EXCEPTION_ENTITY_NOT_FOUND, "Request", requestId);
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.notFound().build();
            
        } catch (Exception e) {
            log.error(LogMessage.CONTROLLER_UNEXPECTED_ERROR, e.getMessage(), e);
            log.error(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            return ResponseEntity.internalServerError().build();
        }
    }
}