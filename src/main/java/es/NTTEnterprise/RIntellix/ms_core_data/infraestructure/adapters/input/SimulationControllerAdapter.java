package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.input;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ArchiveSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CalculatedSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input.SimulationPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes simulation-related endpoints.
 * Acts as the input adapter in the hexagonal architecture for the simulation
 * aggregate.
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
        this.simulationPortService = Objects.requireNonNull(simulationPortService);
    }

    /**
     * GET /api/simulations
     * Retrieves a list of simulations with optional filtering by request ID, party
     * name or party ID, or all the archived simulations.
     * Example: GET /api/simulations?requestId=abc123&partyName=John%20Doe
     *
     * @param requestId the ID of the associated request (optional filter)
     * @param partyName the name of the client (optional filter)
     * @param partyId   the ID of the client (optional filter)
     * @return 200 OK with list of SimulationSummaryDTO, or appropriate error
     *         response.
     */
    @GetMapping
    public ResponseEntity<List<SimulationSummaryDTO>> listSimulations(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean archived) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/simulations");
        log.debug(LogMessage.CONTROLLER_SIMULATION_REQUEST_PARAMS, search, "", "", archived);

        List<SimulationSummaryDTO> simulations = simulationPortService.listSimulations(search, archived);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, 200, simulations.size());
        return ResponseEntity.ok(simulations);
    }

    /**
     * GET /api/simulations/{simulationId}
     * Retrieves the detailed information of a specific simulation, including
     * modified values,
     * simulated results, base scoring results and the computed delta comparison.
     *
     * @param simulationId the unique identifier of the simulation
     * @return 200 OK with SimulationDetailsDTO, 404 if not found, 400 if invalid
     *         ID.
     */
    @GetMapping("/{simulationId}")
    public ResponseEntity<SimulationDetailsDTO> getSimulationDetails(@PathVariable String simulationId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/simulations/" + simulationId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, simulationId);

        SimulationDetailsDTO details = simulationPortService.getSimulationDetails(simulationId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, simulationId);
        return ResponseEntity.ok(details);
    }

    /**
     * PUT /api/simulations/{simulationId}
     * Replaces the simulation template with the provided data.
     * Updates form changes, simulated results, deltas, and associated
     * scoring/party references.
     *
     * @param simulationId the unique identifier of the simulation template
     * @param dto          the complete replacement data
     * @return 200 OK on success, 404 if not found, 400 if invalid input.
     */
    @PutMapping("/{simulationId}")
    public ResponseEntity<Void> updateSimulationTemplate(@PathVariable String simulationId,
            @Valid @RequestBody CalculatedSimulationDTO dto) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "PUT", "/api/simulations/" + simulationId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, simulationId);

        simulationPortService.updateSimulationTemplate(simulationId, dto);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, simulationId);
        return ResponseEntity.ok().build();
    }

    /**
     * PATCH /api/simulations/{simulationId}
     * Performs a soft delete (archive) on a simulation.
     * Only the isArchived field can be modified through this operation.
     *
     * @param simulationId the unique identifier of the simulation to archive
     * @param dto          the patch data containing only the isArchived flag
     * @return 200 OK on success, 404 if not found, 400 if invalid input.
     */
    @PatchMapping("/{simulationId}")
    public ResponseEntity<Void> archiveSimulation(@PathVariable String simulationId,
            @Valid @RequestBody ArchiveSimulationDTO dto) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "PATCH", "/api/simulations/" + simulationId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, simulationId);

        simulationPortService.archiveSimulation(simulationId, dto);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 200, simulationId);
        return ResponseEntity.ok().build();
    }

    /**
     * POST /api/simulations
     * Creates and persists a new simulation with the complete data provided by
     * the frontend after the user has finished the simulation workflow
     * (recalculating, comparing) and decided to save.
     *
     * @param dto the complete simulation data including scenario name
     * @return 201 Created with the location of the new simulation
     */
    @PostMapping
    public ResponseEntity<Void> createSimulation(@Valid @RequestBody CreateSimulationDTO dto) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "POST", "/api/simulations");

        String simulationId = simulationPortService.createSimulation(dto);

        URI location = URI.create("/api/simulations/" + simulationId);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 201, simulationId);
        return ResponseEntity.created(location).build();
    }

    /**
     * Delete the simulation if it is archived before (soft delete).
     * 
     * @param simulationId the unique identifier of the simulation to delete
     * @return 200 OK with the ID of the deleted simulation, 404 if not found, 400
     *         if
     *         invalid input, 400 if trying to delete a non-archived simulation.
     * 
     */
    @DeleteMapping("/{simulationId}")
    public ResponseEntity<String> deleteSimulation(@PathVariable String simulationId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "DELETE", "/api/simulations/" + simulationId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, simulationId);
        String deletedId = simulationPortService.deleteSimulation(simulationId);
        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 204, deletedId);

        return ResponseEntity.ok().body(deletedId);
    }

}
