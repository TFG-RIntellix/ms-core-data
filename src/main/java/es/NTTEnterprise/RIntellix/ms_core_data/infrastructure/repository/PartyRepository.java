package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.PartyEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.projections.PartyNameProjection;

/**
 * Repository interface for Party entities, extending Spring Data MongoDB's
 * MongoRepository.
 * Provides CRUD operations and custom query methods for PartyEntity documents
 * in the MongoDB collection.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
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
    @Query(value = "{ '_id': ?0 }", fields = "{ 'demographics.firstName': 1, 'demographics.lastName': 1 }")
    PartyNameProjection findPartyNameProjectionById(ObjectId partyId);

    /**
     * Retrieves only the demographics name fields for multiple parties.
     * 
     * @param partyIds the list of party IDs
     * @return list of projections with firstName and lastName
     */
    @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'demographics.firstName': 1, 'demographics.lastName': 1 }")
    List<PartyNameProjection> findPartyNameProjectionsByIdIn(List<ObjectId> partyIds);

    /**
     * Retrieves only the IDs (via projection) of parties whose first or last name matches the given regex.
     * 
     * @param search the partial name to search for
     * @return list of projections with ID and demographics names
     */
    @Query(value = "{ $expr: { $regexMatch: { input: { $concat: [ { $ifNull: [ '$demographics.firstName', '' ] }, ' ', { $ifNull: [ '$demographics.lastName', '' ] } ] }, regex: ?0, options: 'i' } } }", fields = "{ '_id': 1 }")
    List<PartyNameProjection> findPartyNameProjectionsByNameMatch(String search);

}
