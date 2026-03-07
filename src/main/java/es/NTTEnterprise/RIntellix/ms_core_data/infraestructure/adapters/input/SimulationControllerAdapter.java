package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.SimulationPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes simulation-related endpoints.
 * Acts as the input adapter in the hexagonal architecture for the simulation aggregate.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Slf4j
@RestController
@RequestMapping("/api/simulations")
public class SimulationControllerAdapter {

    private final SimulationPortService simulationPortService;

    public SimulationControllerAdapter(SimulationPortService simulationPortService) {
        this.simulationPortService = simulationPortService;
    }

    /**
     * GET /api/simulations
     * Retrieves a list of simulations with optional filtering by request ID, party name or party ID.
     * Example: GET /api/simulations?requestId=abc123&partyName=John%20Doe
     *
     * @param requestId the ID of the associated request (optional filter)
     * @param partyName the name of the client (optional filter)
     * @param partyId   the ID of the client (optional filter)
     * @return 200 OK with list of SimulationSummaryDTO, or appropriate error response.
     */
    @GetMapping
    public ResponseEntity<List<SimulationSummaryDTO>> listSimulations(
            @RequestParam(required = false) String requestId,
            @RequestParam(required = false) String partyName,
            @RequestParam(required = false) String partyId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/simulations");
        log.debug(LogMessage.CONTROLLER_SIMULATION_REQUEST_PARAMS, requestId, partyName, partyId);

        try {
            List<SimulationSummaryDTO> simulations = simulationPortService.listSimulations(requestId, partyName, partyId);

            log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, HttpStatus.OK.value(), simulations.size());
            return ResponseEntity.ok(simulations);

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

    /**
     * GET /api/simulations/{simulationId}
     * Retrieves the detailed information of a specific simulation, including modified values,
     * simulated results, base scoring results and the computed delta comparison.
     *
     * @param simulationId the unique identifier of the simulation
     * @return 200 OK with SimulationDetailsDTO, 404 if not found, 400 if invalid ID.
     */
    @GetMapping("/{simulationId}")
    public ResponseEntity<SimulationDetailsDTO> getSimulationDetails(@PathVariable String simulationId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/simulations/" + simulationId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, simulationId);

        try {
            SimulationDetailsDTO details = simulationPortService.getSimulationDetails(simulationId);

            log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, HttpStatus.OK.value(), simulationId);
            return ResponseEntity.ok(details);

        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.CONTROLLER_VALIDATION_ERROR, e.getMessage());
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return ResponseEntity.badRequest().build();

        } catch (EntityNotFoundException e) {
            log.warn(LogMessage.EXCEPTION_ENTITY_NOT_FOUND, "Simulation", simulationId);
            log.warn(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.NOT_FOUND.value(), e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            log.error(LogMessage.CONTROLLER_UNEXPECTED_ERROR, e.getMessage(), e);
            log.error(LogMessage.CONTROLLER_RESPONSE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
            return ResponseEntity.internalServerError().build();
        }
    }
}
