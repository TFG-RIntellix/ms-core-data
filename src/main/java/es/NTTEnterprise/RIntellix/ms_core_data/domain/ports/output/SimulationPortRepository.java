package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.NotArchivedException;

/**
 * Output port for Simulation aggregate persistence.
 * Defines the repository contract for simulation data access.
 * Since this is a READ-ONLY microservice, only retrieval operations are
 * supported.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
public interface SimulationPortRepository {

    /**
     * Retrieves a simulation by its unique identifier.
     *
     * Used by: getSimulationDetails() use case
     * Returns: Complete Simulation aggregate with all nested data
     *
     * @param simulationId the unique identifier of the simulation
     * @return Simulation aggregate with the specified ID
     * @throws IllegalArgumentException if simulationId is null or empty
     * @throws EntityNotFoundException  if no simulation is found with the given ID
     */
    Simulation findById(String simulationId) throws EntityNotFoundException, IllegalArgumentException;

    /**
     * Finds simulations based on a generic search term, a list of matching party IDs, and archive status.
     * 
     * @param search   the generic search term (optional)
     * @param partyIds the list of party IDs to filter by (optional)
     * @param archived the archive status (optional filter)
     * @return list of simulation aggregates matching the provided filters
     */
    List<Simulation> findWithFilters(String search, List<String> partyIds, Boolean archived);

    /**
     * Persists a simulation aggregate to the database.
     * Used by update and archive operations.
     *
     * @param simulation the Simulation domain entity to save
     * @return the saved Simulation domain entity
     */
    Simulation save(Simulation simulation);

    /**
     * Deletes a simulation from the database.
     * Only archived simulations can be deleted; attempting to delete a
     * non-archived simulation will throw NotArchivedException.
     *
     * @param simulationId the unique identifier of the simulation to delete
     * @throws IllegalArgumentException if simulationId is null or empty
     * @throws EntityNotFoundException  if no simulation is found with the given ID
     * @throws NotArchivedException     if the simulation is not archived
     */
    public void delete(String simulationId)
            throws IllegalArgumentException, EntityNotFoundException, NotArchivedException;

    /**
     * Checks if a simulation with the given scenario name already exists for the given request ID.
     *
     * @param requestId the ID of the associated request
     * @param scenarioName the name of the scenario to check
     * @return true if a simulation exists, false otherwise
     */
    boolean existsByRequestIdAndScenarioName(String requestId, String scenarioName);

}
