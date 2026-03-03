package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output;

import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Contract;

/**
 * Output port interface for Contract repository operations.
 * Defines the contract for retrieving Contract entities from the database.
 * Contracts belong to the Party aggregate and are loaded as part of it.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
public interface ContractPortRepository {

    /**
     * Retrieves all contracts associated with a given party.
     *
     * @param partyId the unique identifier of the party
     * @return list of Contract domain entities for the given party
     */
    List<Contract> findByPartyId(String partyId);

    /**
     * Retrieves only the active contracts for a given party.
     * Active contracts are those with status "ACTIVO" and are used
     * for DTI and LTV calculations.
     *
     * @param partyId the unique identifier of the party
     * @return list of active Contract domain entities for the given party
     */
    List<Contract> findActiveByPartyId(String partyId);
}
