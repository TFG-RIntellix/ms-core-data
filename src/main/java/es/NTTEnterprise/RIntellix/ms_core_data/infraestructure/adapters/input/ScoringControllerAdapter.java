package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.ScoringPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes scoring-related endpoints.
 * Acts as the input adapter in the hexagonal architecture.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Slf4j
@RestController
@RequestMapping("/api/requests")
public class ScoringControllerAdapter {

    private final ScoringPortService scoringPortService;

    public ScoringControllerAdapter(ScoringPortService scoringPortService) {
        this.scoringPortService = scoringPortService;
    }

    /**
     * GET /api/requests/{requestId}/scoring
     * Retrieves the current (most recent) scoring associated with a request.
     *
     * @param requestId The ID of the request.
     * @return 200 OK with the ScoringDTO, 404 if not found, 400 if invalid ID.
     */
    @GetMapping("/{requestId}/scoring")
    public ResponseEntity<ScoringDTO> getScoringByRequestId(@PathVariable String requestId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/requests/" + requestId + "/scoring");
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);

        try {

            ScoringDTO scoring = scoringPortService.getScoringByRequestId(requestId);
            log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, HttpStatus.OK.value(), requestId);
            return ResponseEntity.ok(scoring);

        } catch (IllegalArgumentException e) {

            log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, e.getMessage());
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().build();
        
        } catch (EntityNotFoundException e) {
            
            log.warn(LogMessage.EXCEPTION_ENTITY_NOT_FOUND, "Scoring", requestId);
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.notFound().build();
        
        } catch (Exception e) {
            
            log.error(LogMessage.CONTROLLER_UNEXPECTED_ERROR, e.getMessage(), e);
            log.error(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            return ResponseEntity.internalServerError().build();
        }
    }
}
