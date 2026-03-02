package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
/**
 * Output port interface for Party repository operations.
 * This interface defines the contract for retrieving Party aggregates from the database.
 * Since this microservice is read-only, only retrieval methods are defined here.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public interface PartyPortRepository {


    /**
     * Retrieves a party by its unique identifier.
     * 
     * Used by: getPartyDetails() use case
     * Returns: Complete Party aggregate with all nested data
     *
     * @param partyId the unique identifier of the party
     * @return Party aggregate with the specified ID
     * @throws IllegalArgumentException if partyId is null or empty
     * @throws EntityNotFoundException if no party is found with the given ID
     */
    public Party findById(String partyId) throws EntityNotFoundException, IllegalArgumentException;

    /**
     * Retrieves a partial Party aggregate containing only the name information.
     * More efficient than findById when only the party name is needed.
     * 
     * Used by: listRequests() use case for RequestSummaryDTO
     * Returns: Party with Person containing only firstName and lastName
     * 
     * @param partyId the unique identifier of the party
     * @return partial Party with name fields only, or null if not found
     */
    Party findPartyName(String partyId);

}
