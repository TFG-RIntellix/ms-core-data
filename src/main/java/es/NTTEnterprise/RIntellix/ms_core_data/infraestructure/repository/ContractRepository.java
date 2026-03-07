package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ContractEntity;

/**
 * Repository interface for Contract entities in the "contracts" MongoDB
 * collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
public interface ContractRepository extends MongoRepository<ContractEntity, ObjectId> {

    /**
     * Retrieves all contracts associated with a given party.
     *
     * @param partyId the party identifier (ObjectId stored as String)
     * @return list of ContractEntity for the specified party
     */
    @Query("{ 'party_id': ?0 }")
    List<ContractEntity> findByPartyId(ObjectId partyId);

    /**
     * Retrieves contracts for a given party filtered by status.
     * Used to fetch only active contracts for DTI calculation.
     *
     * @param partyId the party identifier (ObjectId to match MongoDB's stored type)
     * @param status  the contract status (e.g., "ACTIVO")
     * @return list of ContractEntity matching the party and status criteria
     */
    @Query("{ 'party_id': ?0, 'status': ?1 }")
    List<ContractEntity> findByPartyIdAndStatus(ObjectId partyId, String status);
}
