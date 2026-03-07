package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

/**
 * Input port for simulation operations.
 * Defines the use cases available for simulation retrieval.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface SimulationPortService {

    /**
     * Retrieves a list of simulations with their basic information, supporting
     * optional filtering
     * by associated request ID, party name or party ID.
     *
     * @param requestId the ID of the associated request (optional filter)
     * @param partyName the name of the client (optional filter, resolved at
     *                  application layer)
     * @param partyId   the ID of the client (optional filter)
     * @return a list of SimulationSummaryDTO objects matching the filters
     */
    List<SimulationSummaryDTO> listSimulations(String requestId, String partyName, String partyId);

    /**
     * Retrieves the detailed information of a specific simulation, including
     * modified values,
     * simulated results, base scoring results and the computed delta comparison.
     *
     * @param simulationId the unique identifier of the simulation
     * @return a SimulationDetailsDTO with the complete simulation detail
     * @throws IllegalArgumentException if the simulationId is null or empty
     * @throws EntityNotFoundException  if no simulation is found with the given ID
     */
    SimulationDetailsDTO getSimulationDetails(String simulationId)
            throws IllegalArgumentException, EntityNotFoundException;

}
