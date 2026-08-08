package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import java.net.URI;
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
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.SimulationPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller that exposes simulation-related endpoints.
 * Acts as the input adapter in the hexagonal architecture for the simulation
 * aggregate.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
@Slf4j
@RestController
@RequestMapping("/api/simulations")
@Tag(name = "Simulations", description = "Endpoints for managing persistent risk simulations")
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
    @Operation(summary = "List all simulations", description = "Retrieves a list of simulations with optional filtering and pagination.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the page of simulations")
    public ResponseEntity<PageResponseDTO<SimulationSummaryDTO>> listSimulations(
            @Parameter(description = "Generic search term for filtering simulations") @RequestParam(required = false) String search,
            @Parameter(description = "Filter by archived status") @RequestParam(required = false) Boolean archived,
            @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of elements per page", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "simulationDate") @RequestParam(defaultValue = "simulationDate") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "GET", "/api/simulations");
        log.debug(LogMessage.CONTROLLER_SIMULATION_REQUEST_PARAMS, search, "", "", archived);

        PageResponseDTO<SimulationSummaryDTO> simulations = 
            simulationPortService.listSimulations(search, archived, page, size, sortBy, sortDir);

        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS, 200, simulations.getContent().size());
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
    @Operation(summary = "Get simulation details", description = "Retrieves the detailed information of a specific simulation, including modified values, simulated results, and deltas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved simulation details"),
            @ApiResponse(responseCode = "400", description = "Invalid simulation ID format"),
            @ApiResponse(responseCode = "404", description = "Simulation not found")
    })
    public ResponseEntity<SimulationDetailsDTO> getSimulationDetails(
            @Parameter(description = "The unique identifier of the simulation", required = true)
            @PathVariable String simulationId) {

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
    @Operation(summary = "Update simulation template", description = "Replaces the simulation template with the provided data. Updates form changes, simulated results, and deltas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the simulation template"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or format"),
            @ApiResponse(responseCode = "404", description = "Simulation not found")
    })
    public ResponseEntity<Void> updateSimulationTemplate(
            @Parameter(description = "The unique identifier of the simulation template", required = true)
            @PathVariable String simulationId,
            @Parameter(description = "The complete replacement data for the simulation", required = true)
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
    @Operation(summary = "Archive simulation", description = "Performs a soft delete (archive) on a simulation. Only the isArchived field can be modified.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully archived or restored the simulation"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Simulation not found")
    })
    public ResponseEntity<Void> archiveSimulation(
            @Parameter(description = "The unique identifier of the simulation to archive", required = true)
            @PathVariable String simulationId,
            @Parameter(description = "The patch data containing the new isArchived flag", required = true)
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
    @Operation(summary = "Create a new simulation", description = "Creates and persists a new simulation with the complete data provided by the frontend.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the new simulation"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createSimulation(
            @Parameter(description = "The complete simulation data including scenario name", required = true)
            @Valid @RequestBody CreateSimulationDTO dto) {

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
    @Operation(summary = "Delete simulation", description = "Permanently deletes a simulation if it was previously archived (soft deleted).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the simulation"),
            @ApiResponse(responseCode = "400", description = "Simulation is not archived and cannot be deleted"),
            @ApiResponse(responseCode = "404", description = "Simulation not found")
    })
    public ResponseEntity<String> deleteSimulation(
            @Parameter(description = "The unique identifier of the simulation to delete", required = true)
            @PathVariable String simulationId) {

        log.info(LogMessage.CONTROLLER_REQUEST_RECEIVED, "DELETE", "/api/simulations/" + simulationId);
        log.debug(LogMessage.CONTROLLER_REQUEST_PATH_VAR, simulationId);
        String deletedId = simulationPortService.deleteSimulation(simulationId);
        log.info(LogMessage.CONTROLLER_RESPONSE_SUCCESS_SINGLE, 204, deletedId);

        return ResponseEntity.ok().body(deletedId);
    }

}
