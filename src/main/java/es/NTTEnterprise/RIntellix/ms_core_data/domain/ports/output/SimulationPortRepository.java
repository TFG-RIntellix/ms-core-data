package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

/**
 * Output port for Simulation aggregate persistence.
 * Defines the repository contract for simulation data access.
 * Since this is a READ-ONLY microservice, only retrieval operations are
 * supported.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
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
     * Retrieves simulations with dynamic filtering.
     * Only non-null parameters are applied as filters.
     * When both parameters are null, all simulations are returned.
     *
     * Note: party name filtering is not possible at the database level because
     * the simulations collection only stores party_id (ObjectId reference).
     * Party name filtering is handled at the application layer (post-fetch).
     *
     * @param requestId the ID of the associated request (optional filter)
     * @param partyId   the ID of the associated party (optional filter)
     * @return List of simulations matching the specified filters
     */
    List<Simulation> findWithFilters(String requestId, String partyId);

}
