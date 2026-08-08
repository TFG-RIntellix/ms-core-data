package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ScoringPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes scoring-related endpoints.
 * Acts as the input adapter in the hexagonal architecture.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/requests")
@Tag(name = "Scorings", description = "Operations related to base and calculated scorings for requests")
public class ScoringControllerAdapter {

    private final ScoringPortService scoringPortService;

    public ScoringControllerAdapter(ScoringPortService scoringPortService) {
        this.scoringPortService = Objects.requireNonNull(scoringPortService);
    }

    /**
     * GET /api/requests/{requestId}/scoring
     * Retrieves the current (most recent) scoring associated with a request.
     *
     * @param requestId The ID of the request.
     * @return 200 OK with the ScoringDTO, 404 if not found, 400 if invalid ID.
     */
    @GetMapping("/{requestId}/scoring")
    @Operation(summary = "Get current scoring", description = "Retrieves the most recent scoring associated with a request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the scoring"),
            @ApiResponse(responseCode = "400", description = "Invalid request ID format"),
            @ApiResponse(responseCode = "404", description = "Scoring or request not found")
    })
    public ResponseEntity<ScoringDTO> getScoringByRequestId(
            @Parameter(description = "The ID of the request", required = true)
            @PathVariable String requestId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/requests/" + requestId + "/scoring");
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, requestId);

        ScoringDTO scoring = scoringPortService.getScoringByRequestId(requestId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, requestId);
        return ResponseEntity.ok(scoring);
    }
}
