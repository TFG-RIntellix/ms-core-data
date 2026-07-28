package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ArchiveSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CalculatedSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateSimulationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;

/**
 * Input port for simulation operations.
 * Defines the use cases available for simulation retrieval.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface SimulationPortService {

        /**
         * Lists simulations based on optional filters.
         * 
         * @param search    a generic search term to match against request ID or party name (optional)
         * @param archived  the archive status of the simulations (optional)
         * @return a list of SimulationSummaryDTO objects matching the given criteria
         */
        List<SimulationSummaryDTO> listSimulations(String search, Boolean archived);

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

        /**
         * Replaces the data of an existing persisted simulation with new calculated
         * data.
         * Updates form changes, simulated results, deltas, and associated
         * scoring/party references.
         *
         * @param simulationId the unique identifier of the simulation to update
         * @param dto          the complete replacement data
         * @throws IllegalArgumentException if the simulationId is null or empty
         * @throws EntityNotFoundException  if no simulation is found with the given ID
         */
        void updateSimulationTemplate(String simulationId, CalculatedSimulationDTO dto)
                        throws IllegalArgumentException, EntityNotFoundException;

        /**
         * Performs a soft delete (archive) on a simulation.
         * Only the isArchived field can be modified through this operation.
         *
         * @param simulationId the unique identifier of the simulation to archive
         * @param dto          the patch data containing the isArchived flag
         * @throws IllegalArgumentException if the simulationId is null or empty,
         *                                  or if isArchived is null
         * @throws EntityNotFoundException  if no simulation is found with the given ID
         */
        void archiveSimulation(String simulationId, ArchiveSimulationDTO dto)
                        throws IllegalArgumentException, EntityNotFoundException;

        /**
         * Creates and persists a new simulation with complete data.
         * Called when the user has finished the simulation workflow (modifying
         * parameters,
         * recalculating via ms-scoring) and decides to save the final result.
         *
         * @param dto the complete simulation data including scenario name
         * @return the ID of the newly created simulation
         * @throws IllegalArgumentException if required fields are missing
         */
        String createSimulation(CreateSimulationDTO dto)
                        throws IllegalArgumentException;

        /**
         * Performs a hard delete on a simulation, removing it permanently from the
         * database.
         * This operation is irreversible and should be used with caution.
         * 
         * @param simulationId the unique identifier of the simulation to delete
         * @return the ID of the deleted simulation for confirmation
         * @throws IllegalArgumentException if the simulationId is null or empty
         * @throws EntityNotFoundException  if no simulation is found with the given ID
         * @throws NotArchivedException     if the simulation is not archived and
         *                                  therefore cannot be deleted
         */
        String deleteSimulation(String simulationId)
                        throws IllegalArgumentException, EntityNotFoundException, NotArchivedException;

}
