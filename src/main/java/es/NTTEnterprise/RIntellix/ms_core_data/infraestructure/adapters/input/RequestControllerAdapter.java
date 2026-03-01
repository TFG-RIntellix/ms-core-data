package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.RequestSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.RequestPortService;
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
        // TODO: Validate user credentials via gatway before doing the petition. 
        System.out.println("Received request to list requests with filters - partyName: " + partyName + ", requestStatus: " + requestStatus);
        try {
            List<RequestSummaryDTO> requests = requestPortService.listRequests(partyName, requestStatus);
            return ResponseEntity.ok(requests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDetailsDTO> getRequestDetails(@PathVariable String requestId) {
        try {
            RequestDetailsDTO details = requestPortService.getRequestDetails(requestId);
            return ResponseEntity.ok(details);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    }