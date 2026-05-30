package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.PartyEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.projections.PartyNameProjection;

/**
 * Repository interface for Party entities, extending Spring Data MongoDB's
 * MongoRepository.
 * Provides CRUD operations and custom query methods for PartyEntity documents
 * in the MongoDB collection.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public interface PartyRepository extends MongoRepository<PartyEntity, ObjectId> {

    /**
     * Retrieves only the demographics name fields (firstName, lastName) for a
     * party.
     * Uses projection for efficient query, avoiding retrieval of the full document.
     * 
     * @param partyId the unique identifier of the party
     * @return projection with firstName and lastName, or null if not found
     */
    @Query(value = "{ '_id': ?0 }", fields = "{ 'demographics.first_name': 1, 'demographics.last_name': 1 }")
    PartyNameProjection findPartyNameProjectionById(ObjectId partyId);

}
